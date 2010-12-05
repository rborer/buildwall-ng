package net.morlhon.wall.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildEventListener;

import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.GridPane;
import org.apache.pivot.wtk.Label;

public class PivotBuildEventListener implements BuildEventListener {

	private final GridPane gridPane;

	private final BlockingQueue<BuildEvent> eventQueue;

	public PivotBuildEventListener(final GridPane gridPane) {
		this.gridPane = gridPane;
		eventQueue = new ArrayBlockingQueue<BuildEvent>(
				gridPane.getColumnCount() * gridPane.getColumnCount());

		new BackgroundWallUpdater().start();
	}

	@Override
	public void publish(final BuildEvent event) {
		eventQueue.add(event);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BuildEvent> status() {
		return new ArrayList<BuildEvent>(eventQueue);
	}

	private class BackgroundWallUpdater extends Thread {

		private int current = 0;
		final int columnCount;

		public BackgroundWallUpdater() {
			super("Background wall updater");
			columnCount = gridPane.getColumnCount();
		}

		@Override
		public void run() {
			try {
				while (true) {
					final BuildEvent event = eventQueue.take();

					final CardPane cardPane = (CardPane) gridPane.get(current);
					final int nextPane = (cardPane.getSelectedIndex() + 1) % 2;

					final Border eventBox = (Border) cardPane.get(nextPane);
					final Label label = (Label) eventBox.getContent();

					label.setText(event.toString());
					setBorderBackground(event, eventBox);

					cardPane.setSelectedIndex(nextPane);

					current = (current + 1) % ((columnCount * columnCount));
					System.out.println("Next current will be " + current);
				}
			} catch (InterruptedException e) {

			}
		}

		private void setBorderBackground(final BuildEvent event,
				final Border border) {

			final String color;

			switch (event.getStatus()) {
			case SUCCESS:
				color = "green";
				break;
			case BUILDING:
				color = "blue";
				break;
			case FAILED:
				color = "red";
				break;
			default:
				color = "gray";
			}

			border.getStyles().put("backgroundColor", color);
		}
	}
}
