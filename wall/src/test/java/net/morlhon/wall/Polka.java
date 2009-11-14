package net.morlhon.wall;

public class Polka {
   
   public static void main(String[] args) {
      System.setProperty("EMPTY", "");
      System.out.println(System.getProperty("EMPTY"));
      System.out.println(System.getProperty("NONEXISTENT"));
   }
   
}
