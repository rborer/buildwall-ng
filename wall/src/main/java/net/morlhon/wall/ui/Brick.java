package net.morlhon.wall.ui;

import java.awt.Color;

/**
 * A Brick is a widget displaying an information as a graphical block.<br>
 * A block's information never change (the data representated by this Brick.<br>
 * But a Brick can be resized, moved graphically.
 *
 * @author Jean-Laurent de Morlhon
 */
public class Brick {
   private final String name;
   private final String comment;
   private final String footer;
   private final Color color;
   /**
    * x coordinate
    */
	public int x;
   /**
    * y coordinate
    */
	public int y;
   /**
    * graphical width
    */
	public int width;
   /**
    * graphical height
    */
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
