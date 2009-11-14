package net.morlhon.wall;

import java.net.URL;
import java.util.Collections;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildStatus;
import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class WallDemo {

   public static void main(String[] args) throws Exception {
      //file:///Users/jlf/Desktop/faces
      System.setProperty("DEBUG", "true");
      Wall wall = new Wall(new URL("http://tux6.vidal.net/faces"), new HorizontalUsherette());
      wall.publish(new BuildEvent("Merlin CD-ROM", BuildStatus.FAILED, "trunk", Collections.singletonList("Jean-Laurent de Morlhon")));
      wall.publish(new BuildEvent("Vidal ID", BuildStatus.BUILDING, "trunk", Collections.singletonList("nicolas")));
      wall.publish(new BuildEvent("Galaad", BuildStatus.SUCCESS, "trunk", Collections.singletonList("cédric")));
      wall.publish(new BuildEvent("Merlin Core", BuildStatus.SUCCESS, "trunk", Collections.singletonList("jphenri")));
      wall.startHttpServer();
   }

}
