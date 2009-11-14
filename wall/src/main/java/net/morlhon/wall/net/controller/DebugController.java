package net.morlhon.wall.net.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.net.WallHttpServer;

public class DebugController extends AbstractController implements Controller {

	public DebugController(WallHttpServer wallServer) {
		super(wallServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	      response.setContentType("text/html");
	      response.setStatus(HttpServletResponse.SC_OK);
	      response.getWriter().println("<h1>Debug</h1>");
	      response.getWriter().println("<h1>Parameters</h1>");
	      Enumeration<String> headerNames = request.getHeaderNames();
	      while (headerNames.hasMoreElements()) {
	         String name = headerNames.nextElement();
	         response.getWriter().println(name + " = " + request.getHeader(name) + "<br/>");
	      }
	      response.getWriter().println("<h2>Attributes</h2>");
	      Enumeration<String> attributeNames = request.getAttributeNames();
	      while (attributeNames.hasMoreElements()) {
	         String name = attributeNames.nextElement();
	         response.getWriter().println(name + " = " + request.getAttribute(name) + "<br/>");
	      }
	      response.getWriter().println("<h1>Parameters</h1>");
	      Enumeration<String> parameterNames = request.getParameterNames();
	      while (parameterNames.hasMoreElements()) {
	         String name = parameterNames.nextElement();
	         response.getWriter().println(name + " = " + request.getParameter(name) + "<br/>");
	      }
	}

}
