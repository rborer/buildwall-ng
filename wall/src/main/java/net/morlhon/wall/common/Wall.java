package net.morlhon.wall.common;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.morlhon.wall.net.WallHttpServer;
import net.morlhon.wall.ui.Brick;
import net.morlhon.wall.ui.WallFrame;
import net.morlhon.wall.ui.ushering.Usherette;

public class Wall implements BuildEventListener, GuiEventListener {
	private static final Color successColor = new Color(0, 0x80, 0);
	private static final Color failedColor = Color.RED;
	private static final Color buildingColor = Color.YELLOW;
	private final Map<String, BuildEvent> eventMap = new HashMap<String, BuildEvent>();
	private static final int MAX_EVENT = 9;
	private static final int DEFAULT_PORT = 8080;
	private WallFrame frame;
	private WallHttpServer server;

	public void startGUI(URL faceUrl, Usherette usherette) {
		frame = new WallFrame(this, faceUrl, usherette);
	}

	public void startHttpServer() {
		server = new WallHttpServer(DEFAULT_PORT, this);
		server.start();
	}

	public void startHttpServer(int port) {
		server = new WallHttpServer(port, this);
		server.start();
	}

	@Override
	public void publish(BuildEvent event) {
		synchronized (eventMap) {
			eventMap.put(event.getName(), event);
		}
		frame.setBrickList(createBrickListFromEventMap());
	}

	@Override
	public void reset() {
		synchronized (eventMap) {
			eventMap.clear();
		}
		frame.setBrickList(createBrickListFromEventMap());
	}

	// Transform a list of BuildEvent into a Brick List.
	private List<Brick> createBrickListFromEventMap() {
		Map<String, BuildEvent> eventMapCopy = null;
		synchronized (eventMap) {
			eventMapCopy = new HashMap<String, BuildEvent>(eventMap);
		}
		List<Brick> newBrickList = new ArrayList<Brick>(eventMap.size());
		List<BuildEvent> buildEventList = new ArrayList<BuildEvent>(
				eventMapCopy.values());
		Collections.sort(buildEventList);
		int max = Math.min(buildEventList.size(), MAX_EVENT);
		for (int i = 0; i < max; i++) {
			BuildEvent currentEvent = buildEventList.get(i);
			Color color = failedColor;
			switch (currentEvent.getStatus()) {
			case SUCCESS:
				color = successColor;
				break;
			case BUILDING:
				color = buildingColor;
				break;
			}
			Brick brick = new Brick(currentEvent.getName(), color,
					currentEvent.getCategory(),
					collection2String(currentEvent.getAuthors()));
			newBrickList.add(brick);
		}
		return newBrickList;
	}

	private String collection2String(Collection<String> authors) {
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

	@Override
	public List<BuildEvent> status() {
		synchronized (eventMap) {
			return new ArrayList<BuildEvent>(eventMap.values());
		}
	}

	@Override
	public void reload() {
		frame.setBrickList(createBrickListFromEventMap());
		frame.reload();
	}

	@Override
	public void askForShutdown() {
		server.shutdown();
		frame.close();
	}
}
