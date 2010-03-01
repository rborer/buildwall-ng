package net.morlhon.wall.ui.ushering;

import java.util.List;

import net.morlhon.wall.ui.Brick;

public class HorizontalUsherette implements Usherette {

   private static final double COLUMN = 3.0;

   @Override
   public void usher(List<Brick> brickList, int screenWidth, int screenHeight) {
      if (brickList == null) {
         return;
      }
      if (brickList.size() == 0) {
         return;
      }
      int row = (int) Math.ceil(brickList.size() / COLUMN);
      int col = (int) Math.ceil(brickList.size() / (double) row);
      int width = screenWidth / col;
      int height = screenHeight / row;
      for (int i = 0; i < brickList.size(); i++) {
         Brick brick = brickList.get(i);
         brick.x = (i % col) * width;
         brick.y = (i / col) * height;
         brick.width = width;
         brick.height = height;
      }
   }

}
