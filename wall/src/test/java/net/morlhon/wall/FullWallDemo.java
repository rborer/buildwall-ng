package net.morlhon.wall;

import java.net.URL;

import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class FullWallDemo {

   public static void main(String[] args) throws Exception {
      System.setProperty("DEBUG", "true");
      Wall wall = new Wall(new URL("file:///Users/jlf/Desktop/faces"), new HorizontalUsherette());
      wall.startHttpServer();
   }

}
