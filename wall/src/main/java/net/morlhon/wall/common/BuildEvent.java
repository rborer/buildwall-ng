package net.morlhon.wall.common;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a build event as issued by the build system.<br>
 * This object is meant to be a read-only representation of a build event.
 *
 * @author Jean-Laurent de Morlhon
 */
public class BuildEvent implements Comparable<BuildEvent> {
	private Date date;
	private String name;
	private BuildStatus status;
	private String category;
	private Set<String> authors;

	public BuildEvent(String project, BuildStatus status) {
		init(project, status, null, null);
	}

	public BuildEvent(String project, BuildStatus status, String category) {
		init(project, status, category, null);
	}

	public BuildEvent(String project, BuildStatus status, String category,
			Set<String> authors) {
		init(project, status, category, authors);
	}

	private void init(String project, BuildStatus status, String category,
			Set<String> authors) {
		this.name = project;
		this.status = status;
		this.date = new Date();
		this.category = category;
		this.authors = authors;
	}

	/**
	 * The optional category of this build event.
	 *
	 * @return the category of this build event as a String or null if none is
	 *         set.
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * The date at which this event has been registered from the build system.
	 *
	 * @return the date of this build event.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * The name/short description of this build event.
	 *
	 * @return the name of this build event.
	 */
	public String getName() {
		return name;
	}

	public BuildStatus getStatus() {
		return status;
	}

	/**
	 * An optional set of authors related having participated to this build
	 * event.
	 *
	 * @return a Set of authors related to this build event, or null if none
	 *         have been set.
	 */
	public Set<String> getAuthors() {
		return authors;
	}

	@Override
	public int compareTo(BuildEvent event) {
		if (event == null) {
			return 1;
		}
		return event.getDate().compareTo(getDate());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("date", date)
				.append("name", name).append("status", status)
				.append("category", category).append("authors", authors)
				.toString();
	}
}
