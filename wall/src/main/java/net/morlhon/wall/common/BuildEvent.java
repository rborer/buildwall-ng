package net.morlhon.wall.common;

import java.util.Date;
import java.util.List;

public class BuildEvent implements Comparable<BuildEvent> {
	private Date date;
	private String name;
	private BuildStatus status;
	private final String category;
	private List<String> authors;

	public BuildEvent(String name, BuildStatus status) {
		this.name = name;
		this.status = status;
		this.date = new Date();
		this.category = null;
	}

	public BuildEvent(String project, BuildStatus status, String category) {
		this.name = project;
		this.status = status;
		this.date = new Date();
		this.category = category;
	}

	public BuildEvent(String project, BuildStatus status, String category,
			List<String> authors) {
		this.name = project;
		this.status = status;
		this.date = new Date();
		this.category = category;
		this.authors = authors;
	}

	public String getCategory() {
		return this.category;
	}

	public Date getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public BuildStatus getStatus() {
		return status;
	}

	public List<String> getAuthors() {
		return authors;
	}

	@Override
	public int compareTo(BuildEvent event) {
		if (event == null) {
			return 1;
		}
		return event.getDate().compareTo(getDate());
	}

}
