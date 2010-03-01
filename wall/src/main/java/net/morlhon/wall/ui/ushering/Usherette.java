package net.morlhon.wall.ui.ushering;

import java.util.List;

import net.morlhon.wall.ui.Brick;

/**
 * An Usherette goal is to place Brick on the screen.
 *
 * @author Jean-Laurent de Morlhon
 */
public interface Usherette {

   /**
    * Modify the brickList given in parameter to place them according to the screen width & height
    *
    * @param brickList a List<Brick> which coordinate will be modified by the usher process.
    * @param screenWidth the screen width within which brick will be placed.
    * @param screenHeight the screen height within which brick will be placed.
    */
   public void usher(List<Brick> brickList, int screenWidth, int screenHeight);

}
