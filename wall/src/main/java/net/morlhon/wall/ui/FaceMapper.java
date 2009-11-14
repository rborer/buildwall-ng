package net.morlhon.wall.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FaceMapper {
   private final URL faceFileURL;
   private final URL baseURL;
   private final Map<String, URL> map;

   public FaceMapper(URL faceURL) {
      this.baseURL = faceURL;
      this.map = new HashMap<String, URL>(10);
      this.faceFileURL = buildURL();
   }

   public void init() {
      try {
         String buffer;
         BufferedReader reader = new BufferedReader(new InputStreamReader(faceFileURL.openStream()));
         while ((buffer = reader.readLine()) != null) {
            StringURL stringURL = buildStringURL(buffer);
            if (stringURL != null) {
               map.put(stringURL.getName(), stringURL.getUrl());
            }
         }
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public URL getUrl(String name) {
      if (name == null) {
         return null;
      }
      return map.get(name.toLowerCase());
   }

   private URL buildURL() {
      try {
         return new URL(baseURL.toString() + "/faces.txt");
      } catch (MalformedURLException mue) {
         return null;
      }
   }

   private StringURL buildStringURL(String buffer) {
      int index = buffer.indexOf("=");
      if (index == -1) {
         return null;
      } else {
         String name = buffer.substring(0, index).trim().toLowerCase();
         String endURl = buffer.substring(index + 1).trim();
         try {
            return new StringURL(name, new URL(baseURL.toString() + "/" + endURl));
         } catch (MalformedURLException mue) {
            return null;
         }
      }
   }

}

class StringURL {
   private String name;
   private URL url;

   public StringURL(String name, URL url) {
      this.name = name;
      this.url = url;
   }

   public String getName() {
      return name;
   }

   public URL getUrl() {
      return url;
   }

}
