package net.morlhon.wall.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.morlhon.wall.common.MD5Util;

public class FaceMapper {
   private static final String URL_GRAVATAR = "http://www.gravatar.com/avatar/";
   private static final String GRAVATAR_SIZE = "?s=200";
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
      if (baseURL == null) {
         return null;
      }
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
            if (endURl.indexOf("@") == -1) {
               return new StringURL(name, new URL(baseURL.toString() + "/" + endURl));
            } else {
               // Gravatar
               return new StringURL(name, new URL(URL_GRAVATAR + MD5Util.md5Hex(endURl) + GRAVATAR_SIZE));
            }
         } catch (MalformedURLException mue) {
            return null;
         }
      }
   }

}

class StringURL {
   private final String name;
   private final URL url;

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
