package net.morlhon.wall.net.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.net.WallHttpServer;

public class StatusController extends AbstractController implements Controller {

   public StatusController(WallHttpServer wallServer) {
      super(wallServer);
   }

   @Override
   public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>Server Status</h1>");
      response.getWriter().println("");
      List<BuildEvent> eventList = wallServer.status();
      for (BuildEvent event : eventList) {
         response.getWriter().println("<blockquote>");
         response.getWriter().println(event.getDate() + "<br/>");
         response.getWriter().println(event.getName() + "<br/>");
         response.getWriter().println(event.getCategory() + "<br/>");
         response.getWriter().println((event.getStatus().toString()) + "<br/>");
         response.getWriter().println("</blockquote>");
      }
   }

}
