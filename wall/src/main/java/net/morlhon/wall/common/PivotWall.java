package net.morlhon.wall.common;

import java.io.IOException;

import net.morlhon.wall.net.WallHttpServer;
import net.morlhon.wall.ui.PivotBuildEventListener;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.GridPane;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtkx.WTKXSerializer;

public class PivotWall implements Application {

	private static final int DEFAULT_NB_COLUMNS = 3;

	private static final String NB_COLUMNS_PARAM = "columns";
	private static final String HTTP_SERVER_PORT = "httpPort";

	private Window window;

	private WallHttpServer server;

	@Override
	public void startup(final Display display,
			final Map<String, String> properties) throws Exception {

		final WTKXSerializer wtkxSerializer = new WTKXSerializer();
		window = (Window) wtkxSerializer.readObject(this, "/layout.wtkx");
		final GridPane gridPane = (GridPane) wtkxSerializer.get("gridPane");
		final CardPane tickerCardPane = (CardPane) wtkxSerializer
				.get("tickerCardPane");

		final int nbColumns = getIntProperty(properties, NB_COLUMNS_PARAM,
				DEFAULT_NB_COLUMNS);
		prepareGridColumns(wtkxSerializer, gridPane, nbColumns,
				display.getHeight() - tickerCardPane.getPreferredHeight() - 26);

		window.open(display);

		final BuildEventListener eventListener = new PivotBuildEventListener(
				gridPane);

		final int serverPort = getIntProperty(properties, HTTP_SERVER_PORT,
				WallHttpServer.DEFAULT_PORT);
		startHttpServer(serverPort, eventListener);
	}

	private void prepareGridColumns(final WTKXSerializer wtkxSerializer,
			final GridPane gridPane, final int nbColumns, final int height)
			throws IOException, SerializationException {
		gridPane.setColumnCount(nbColumns);

		for (int i = 0; i < nbColumns; i++) {
			final GridPane.Row row = new GridPane.Row();
			gridPane.getRows().add(row);
			for (int j = 0; j < nbColumns; j++) {
				row.insert((Component) wtkxSerializer.readObject(this,
						"/event_box.vtkx"), j);
			}
		}

		gridPane.setMinimumPreferredHeight(height);
		gridPane.setMaximumPreferredHeight(height);
	}

	private void startHttpServer(final int serverPort,
			final BuildEventListener eventListener) {
		server = new WallHttpServer(serverPort, eventListener);
		server.start();
	}

	private int getIntProperty(final Map<String, String> properties,
			final String key, final int defaultValue) {

		final String value = properties.get(key);
		if (NumberUtils.isNumber(value)) {
			return NumberUtils.toInt(value);
		} else {
			return defaultValue;
		}
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		if (server != null) {
			server.shutdown();
		}

		if (window != null) {
			window.close();
		}

		return false;
	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}
}
