package net.morlhon.wall.net;

import java.util.Collections;
import java.util.List;

import net.morlhon.wall.common.BuildEvent;
import net.morlhon.wall.common.BuildEventListener;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class WallHttpServer {
   private Server server;
   private BuildEventListener listener;
   private int port;

   public WallHttpServer(int port, BuildEventListener listener) {
      this.listener = listener;
      this.port = port;
   }

   public void start() {
      server = new Server(port);
      Context context = new Context(server, "/", Context.SESSIONS);
      context.addServlet(new ServletHolder(new WallServlet(this)), "/*");
      try {
         server.start();
         server.join();
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }
   }

   public void stop() {
      try {
         server.stop();
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }
   }

   public void registerEvent(BuildEvent event) {
      if (listener != null) {
         listener.publish(event);
      }
   }

   public void reset() {
      if (listener != null) {
         listener.reset();
      }
   }

   public List<BuildEvent> status() {
      if (listener != null) {
         return listener.status();
      }
      return Collections.emptyList();
   }

   public void reload() {
      if (listener != null) {
         listener.reload();
      }
   }

   public int getPort() {
      return port;
   }

}
