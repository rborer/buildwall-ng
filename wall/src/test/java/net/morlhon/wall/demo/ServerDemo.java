package net.morlhon.wall.demo;

import net.morlhon.wall.net.WallHttpServer;

public class ServerDemo {
   
   public static void main(String[] args) throws Exception {
      new WallHttpServer(8080,null).start();
   }
   
}
