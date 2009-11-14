package net.morlhon.wall.common;

import java.util.List;

public interface BuildEventListener {

   public void publish(BuildEvent event);

   public void reset();
   
   public void reload();

   public List<BuildEvent> status();
}
