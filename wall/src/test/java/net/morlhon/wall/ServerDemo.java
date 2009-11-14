package net.morlhon.wall;

import net.morlhon.wall.net.WallHttpServer;

public class ServerDemo {
   
   public static void main(String[] args) throws Exception {
      new WallHttpServer(null).start();
   }
   
}
