package net.morlhon.wall.demo;

import java.net.URL;
import java.util.Collections;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildStatus;
import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class WallDemo {

   public static void main(String[] args) throws Exception {
      URL faceUrl = FullWallDemo.class.getClassLoader().getResource("faces");
      System.setProperty("DEBUG", "true");
      Wall wall = new Wall(faceUrl, new HorizontalUsherette());
      wall.publish(new BuildEvent("Project A", BuildStatus.FAILED, "trunk", Collections.singletonList("Arthur")));
      wall.publish(new BuildEvent("Project B", BuildStatus.BUILDING, "trunk", Collections.singletonList("Barnabe")));
      wall.publish(new BuildEvent("Project C", BuildStatus.SUCCESS, "trunk", Collections.singletonList("Charles")));
      wall.publish(new BuildEvent("Project D", BuildStatus.SUCCESS, "trunk", Collections.singletonList("Dorothe")));
      wall.startHttpServer();
   }

}
