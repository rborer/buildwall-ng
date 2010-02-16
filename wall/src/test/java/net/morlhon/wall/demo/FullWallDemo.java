package net.morlhon.wall.demo;

import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class FullWallDemo {

   public static void main(String[] args) throws Exception {
      System.setProperty("WINDOWED", "true");
      Wall wall = new Wall(null, new HorizontalUsherette());
      wall.startHttpServer();
   }

}
