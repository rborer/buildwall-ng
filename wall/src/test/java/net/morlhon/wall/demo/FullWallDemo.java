package net.morlhon.wall.demo;

import java.net.URL;

import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class FullWallDemo {

   public static void main(String[] args) throws Exception {
      URL faceUrl = FullWallDemo.class.getClassLoader().getResource("faces");
      System.setProperty("DEBUG", "true");
      Wall wall = new Wall(faceUrl, new HorizontalUsherette());
      wall.startHttpServer();
   }

}
