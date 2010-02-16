package net.morlhon.wall;

import java.net.MalformedURLException;
import java.net.URL;

import net.morlhon.wall.ui.Wall;
import net.morlhon.wall.ui.ushering.HorizontalUsherette;

/**
 * Startups a default http wall server.
 *
 * @author jlf
 */
public class BuildRadiator {
   /*package*/int port = 8080;
   /*package*/URL url = null;

   public static void main(String[] args) throws MalformedURLException {
      BuildRadiator radiator = new BuildRadiator(args);
      radiator.startup();
   }

   public BuildRadiator(String[] args) throws MalformedURLException {
      if (args.length > 0) {
         for (int i = 0; i < args.length; i++) {
            try {
               port = Integer.parseInt(args[i]);
            } catch (NumberFormatException nfe) {
               url = new URL(args[i]);
            }
         }
      }
   }

   public void startup() {
      Wall wall = new Wall(url, new HorizontalUsherette());
      wall.startHttpServer(port);
   }

}
