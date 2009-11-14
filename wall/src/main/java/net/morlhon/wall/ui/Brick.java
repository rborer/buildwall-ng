package net.morlhon.wall.ui;

import java.awt.Color;

public class Brick {
	private String name;
	private String comment;
	private String footer;
	private Color color;
	public int x;
	public int y;
	public int width;
	public int height;

	public Brick(String name, Color color, String comment, String footer) {
		this.name = name;
		this.color = color;
		this.comment = comment;
		this.footer = footer;
	}
	
	public void reset() {
	   x = y = width = height = -1;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public String getComment() {
		return comment;
	}
	
	public String getFooter() {
		return footer;
	}

	@Override
	public String toString() {
		return "Brick [color=" + color + ", height=" + height + ", name="
				+ name + ", width=" + width + ", x=" + x + ", y=" + y + "]";
	}

}
