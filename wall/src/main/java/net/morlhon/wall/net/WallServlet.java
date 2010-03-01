package net.morlhon.wall.net;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.net.controller.HomePageController;
import net.morlhon.wall.net.controller.PublishController;
import net.morlhon.wall.net.controller.ReloadController;
import net.morlhon.wall.net.controller.ResetController;
import net.morlhon.wall.net.controller.StatusController;

public class WallServlet extends HttpServlet {
   private final WallHttpServer wallServer;

   public WallServlet(WallHttpServer wallServer) {
      this.wallServer = wallServer;
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String contextPath = request.getPathInfo();
      if (contextPath.startsWith("/publish")) {
         new PublishController(wallServer).process(request, response);
         return;
      }
      if (contextPath.startsWith("/status")) {
         new StatusController(wallServer).process(request, response);
         return;
      }
      if (contextPath.startsWith("/reset")) {
         new ResetController(wallServer).process(request, response);
         return;
      }
      if (contextPath.startsWith("/reload")) {
         new ReloadController(wallServer).process(request, response);
         return;
      }
      new HomePageController(wallServer).process(request, response);
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
