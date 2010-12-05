package net.morlhon.wall.net.controller;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildStatus;
import net.morlhon.wall.net.WallHttpServer;

public class PublishController extends AbstractController implements Controller {

	public PublishController(WallHttpServer wallServer) {
		super(wallServer);
	}

	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String project = request.getParameter("project");
		String status = request.getParameter("status");
		// optional
		String category = request.getParameter("category");
		// optional
		String authors = request.getParameter("authors");

		if (project == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>Event Published</h1>");
		response.getWriter().println("project =" + project + "<br/>");
		response.getWriter().println("status =" + status + "<br/>");
		response.getWriter().println("category =" + category + "<br/>");
		response.getWriter().println("authors =" + authors + "<br/>");
		publishToServer(project, status, category, authors);
	}

	private void publishToServer(String project, String status,
			String category, String authors) {
		BuildStatus buildStatus = BuildStatus.valueOf(status.toUpperCase());
		wallServer.registerEvent(new BuildEvent(project, buildStatus, category,
				stringToSet(authors)));
	}

	private Set<String> stringToSet(String authors) {
		Set<String> set = new LinkedHashSet<String>();
		if (authors != null) {
			for (String author : authors.split(",")) {
				set.add(author.trim());
			}
		}
		return set;
	}

}
