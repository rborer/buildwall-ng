package net.morlhon.wall.ui.ushering;

import java.util.List;

import net.morlhon.wall.ui.Brick;

public class VerticalUsherette implements Usherette {

   @Override
   public void usher(List<Brick> brickList, int screenWidth, int screenHeight) {
      if (brickList == null) {
         return;
      }
      if (brickList.size() == 0) {
         return;
      }
      int row = numberOfRow(brickList.size());
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

   private int numberOfRow(int size) {
      if (size < 4) {
         return 1;
      }
      if (size < 11) {
         return 2;
      }
      if (size < 16) {
         return 3;
      }
      return 4;
   }

}
