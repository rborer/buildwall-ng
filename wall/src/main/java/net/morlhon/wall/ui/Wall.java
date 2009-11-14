package net.morlhon.wall.ui;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildEventListener;
import net.morlhon.wall.net.WallHttpServer;
import net.morlhon.wall.ui.ushering.Usherette;

public class Wall extends WallFrame implements BuildEventListener {
   private static final Color successColor = new Color(0, 0x80, 0);
   private static final Color failedColor = Color.RED;
   private static final Color buildingColor = Color.YELLOW;
   private Map<String, BuildEvent> eventMap = new HashMap<String, BuildEvent>();
   private static final int MAX_EVENT = 9;

   public Wall(URL faceUrl, Usherette usherette) {
      super(faceUrl, usherette);
   }

   public void startHttpServer() {
      WallHttpServer server = new WallHttpServer(this);
      setWallServer(server);
   }

   @Override
   public void publish(BuildEvent event) {
      synchronized (eventMap) {
         eventMap.put(event.getName(), event);
      }
      setBrickList(createBrickListFromEventMap());
   }

   @Override
   public void reset() {
      synchronized (eventMap) {
         eventMap.clear();
      }
      setBrickList(createBrickListFromEventMap());
   }

   private List<Brick> createBrickListFromEventMap() {
      Map<String, BuildEvent> eventMapCopy = null;
      synchronized (eventMap) {
         eventMapCopy = new HashMap<String, BuildEvent>(eventMap);
      }
      List<Brick> newBrickList = new ArrayList<Brick>(eventMap.size());
      List<BuildEvent> buildEventList = new ArrayList<BuildEvent>(eventMapCopy.values());
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
         Brick brick = new Brick(currentEvent.getName(), color, currentEvent.getCategory(), list2String(currentEvent.getAuthors()));
         newBrickList.add(brick);
      }
      return newBrickList;
   }

   private String list2String(List<String> authors) {
      if (authors == null) {
         return "";
      }
      StringBuilder stringBuilder = new StringBuilder();
      for (String string : authors) {
         stringBuilder.append(string);
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
      setBrickList(createBrickListFromEventMap());
      super.reload();
   }
}
