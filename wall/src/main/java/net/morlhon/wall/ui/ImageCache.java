package net.morlhon.wall.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache {
   private final Map<String, BufferedImage> cache;
   private final FaceMapper faceMapper;

   public ImageCache(URL facesURL) {
      this.faceMapper = new FaceMapper(facesURL);
      cache = new HashMap<String, BufferedImage>();
   }

   public void init() {
      faceMapper.init();
      cache.clear();
   }

   public BufferedImage getImage(String key) {
      BufferedImage image = cache.get(key);
      if (image != null) {
         return image;
      }
      image = loadImage(key);
      if (image == null) {
         return null;
      }
      cache.put(key, image);
      return image;
   }

   private BufferedImage loadImage(String key) {
      URL url = faceMapper.getUrl(key);
      if (url == null) {
         return null;
      }
      try {
         return ImageIO.read(url);
      } catch (IOException e) {
         return null;
      }
   }
}
