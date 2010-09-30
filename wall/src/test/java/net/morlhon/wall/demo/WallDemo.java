package net.morlhon.wall.demo;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildStatus;
import net.morlhon.wall.common.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

public class WallDemo {

   public static void main(String[] args) throws Exception {
      URL faceUrl = FullWallDemo.class.getClassLoader().getResource("faces");
      System.setProperty("WINDOWED", "true");
      Wall wall = new Wall();
      wall.startGUI(faceUrl, new HorizontalUsherette());
      wall.publish(new BuildEvent("Project A", BuildStatus.FAILED, "trunk", Collections.singleton("Arthur")));
      wall.publish(new BuildEvent("Project B", BuildStatus.BUILDING, "trunk", Collections.singleton("Barnabe")));
      wall.publish(new BuildEvent("Project C", BuildStatus.SUCCESS, "trunk", Collections.singleton("Charles")));
      wall.publish(new BuildEvent("Project D", BuildStatus.SUCCESS, null, Collections.singleton("Dorothe")));
      wall.publish(new BuildEvent("Project Gravatar", BuildStatus.SUCCESS, null, Collections.singleton("jblemee")));
      wall.publish(new BuildEvent("Project Multi names", BuildStatus.SUCCESS, null, new HashSet<String>(Arrays.asList(new String[] {"jblemee", "Dorothe",
            "John", "Tony", "Karim"}))));
      wall.publish(new BuildEvent("Project No Image", BuildStatus.SUCCESS, null, Collections.singleton("Romain")));
      wall.startHttpServer();
   }

}
