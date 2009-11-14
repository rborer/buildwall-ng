package net.morlhon.wall.net.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.net.WallHttpServer;

public class ReloadController extends AbstractController implements Controller {

   public ReloadController(WallHttpServer wallServer) {
      super(wallServer);
   }

   @Override
   public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>Ordering wall to reload.</h1>");
      response.getWriter().println("It should be done quickly.");
      wallServer.reload();
   }

}
