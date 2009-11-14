package net.morlhon.wall.net.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.net.WallHttpServer;

public class StopController extends AbstractController implements Controller {

   public StopController(WallHttpServer wallServer) {
      super(wallServer);
   }

   @Override
   public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>Ordering Server to Stop.</h1>");
      response.getWriter().println("It doesn't work, work in progress...");
      //wallServer.stop();
   }

}
