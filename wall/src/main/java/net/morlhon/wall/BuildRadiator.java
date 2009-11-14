package net.morlhon.wall;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class BuildRadiator {

   public static void main(String[] args) throws InterruptedException, InvocationTargetException, MalformedURLException {
      URL url;
      if (args.length == 0) {
         url = new URL("file:///Users/jlf/Desktop/faces");
      } else {
         url = new URL(args[0]);
      }
      Wall wall = new Wall(url, new HorizontalUsherette());
      wall.startHttpServer();
   }
}
