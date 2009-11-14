package net.morlhon.wall.net.controller;

import net.morlhon.wall.net.WallHttpServer;

public abstract class AbstractController {
   protected final WallHttpServer wallServer;

   public AbstractController(WallHttpServer wallServer) {
      this.wallServer = wallServer;
   }

}
