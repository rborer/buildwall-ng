package net.morlhon.wall.net.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.net.WallHttpServer;

public class HomePageController extends AbstractController implements Controller {

   public HomePageController(WallHttpServer wallServer) {
      super(wallServer);
   }

   @Override
   public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>BuildWall HomePage</h1>");
      response.getWriter().println("<ul><li>");
      response.getWriter().println("<a href=\"/status\">Status</a> (Show Server current status)");
      response.getWriter().println("</li><li>");
      response.getWriter().println("<a href=\"/reload\">Reload</a> (Reload faces and redraws)");
      response.getWriter().println("</li><li>");
      response.getWriter().println("<a href=\"/reset\">Reset</a> (Reset all data)");
      response.getWriter().println("</li></ul>");
      response.getWriter().println("<p>Visit <a href=\"http://code.google.com/p/buildwall/\"> project home page</a> on the internet.</p>");
   }

}
