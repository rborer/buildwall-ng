package net.morlhon.wall.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildEventListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.Component;
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

		private final FastDateFormat dateFormatter = FastDateFormat
				.getInstance("dd MMM @ HH:mm");

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

					final Border eventBorder = (Border) cardPane.get(nextPane);

					System.out.println("Event border preferred size:"
							+ eventBorder.getPreferredSize());
					System.out.println("Event border size:"
							+ eventBorder.getSize());

					drawBuildEvent(event, eventBorder);

					cardPane.setSelectedIndex(nextPane);

					current = (current + 1) % ((columnCount * columnCount));
				}
			} catch (InterruptedException e) {

			}
		}

		private void drawBuildEvent(final BuildEvent event,
				final Border eventBorder) {

			final BoxPane boxPane = (BoxPane) eventBorder.getContent();
			final Label projectName = (Label) boxPane.get(0);
			final Label projectCategory = (Label) boxPane.get(1);
			final Label authors = (Label) boxPane.get(2);
			final Label buildDate = (Label) boxPane.get(3);

			System.out.println("BoxPane preferred size:"
					+ boxPane.getPreferredSize());

			setBorderBackground(event, eventBorder);

			final int buildDateHeight = drawBuildDate(event, buildDate);

			final int authorsHeight = drawAuthors(event, authors);

			final int projectCategoryHeight = drawCategory(event,
					projectCategory);

			drawProjectName(projectName, buildDateHeight, authorsHeight,
					projectCategoryHeight, eventBorder, boxPane, event);

		}

		private int drawBuildDate(final BuildEvent event, final Label buildDate) {
			final Date currentDate = event.getDate() == null ? new Date()
					: event.getDate();

			buildDate.setText(dateFormatter.format(currentDate));
			buildDate.getStyles().put("color", Color.darkGray);

			return buildDate.getPreferredHeight();
		}

		private int drawAuthors(final BuildEvent event, final Label authors) {
			if (CollectionUtils.isEmpty(event.getAuthors())) {
				authors.setText("");
				fixComponentHeight(authors, 0);
				return 0;
			} else {
				authors.setText("By " + collection2String(event.getAuthors()));
				return authors.getPreferredHeight();
			}
		}

		private int drawCategory(final BuildEvent event,
				final Label projectCategory) {

			if (StringUtils.isEmpty(event.getCategory())) {
				projectCategory.setText("");
				fixComponentHeight(projectCategory, 0);
				return 0;

			} else {
				projectCategory.setText(event.getCategory());
				final Font font = (Font) projectCategory.getStyles()
						.get("font");
				assert font != null;

				fixComponentHeight(projectCategory, font.getSize());
				return font.getSize();
			}
		}

		private void drawProjectName(final Label projectName,
				final int buildDateHeight, final int authorsHeight,
				final int projectCategoryHeight, final Border eventBorder,
				final BoxPane boxPane, final BuildEvent event) {

			int projectNameHeight = eventBorder.getHeight() - buildDateHeight
					- authorsHeight - projectCategoryHeight - 5
					* (Integer) boxPane.getStyles().get("spacing");

			projectName.setText(event.getName());
			fixComponentHeight(projectName, projectNameHeight);
		}

		private void setBorderBackground(final BuildEvent event,
				final Border border) {

			final String color;

			switch (event.getStatus()) {
			case SUCCESS:
				color = "#00BB00";
				break;
			case BUILDING:
				color = "blue";
				break;
			case FAILED:
				color = "#BB0000";
				break;
			default:
				color = "gray";
			}

			border.getStyles().put("backgroundColor", color);
		}

		private void fixComponentHeight(final Component component,
				final int height) {

			assert component != null;
			assert height >= 0;

			System.out.println("Forcing " + component + " to height " + height);

			component.setMinimumPreferredHeight(height);
			component.setMaximumPreferredHeight(height + 1);
		}

		private String collection2String(final Collection<String> authors) {
			if (authors == null) {
				return "";
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (String string : authors) {
				stringBuilder.append(string);
				if (authors.size() > 1) {
					stringBuilder.append("\n");
				}
			}
			return stringBuilder.toString();
		}
	}
}
