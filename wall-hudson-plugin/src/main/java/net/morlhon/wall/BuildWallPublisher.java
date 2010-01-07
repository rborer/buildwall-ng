package net.morlhon.wall;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Project;
import hudson.model.Result;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.kohsuke.stapler.DataBoundConstructor;

public class BuildWallPublisher extends Notifier {
   private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(BuildWallPublisher.class.getName());

   @Extension
   public static final BuildWallDescriptor DESCRIPTOR = new BuildWallDescriptor();

   @DataBoundConstructor
   public BuildWallPublisher() {
      super();
   }

   public BuildStepMonitor getRequiredMonitorService() {
      return BuildStepMonitor.BUILD;
   }

   @Override
   public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
      Result result = build.getResult();
      ChangeLogSet<? extends Entry> changeSet = build.getChangeSet();
      StringBuilder builder = new StringBuilder();
      for (Entry entry : changeSet) {
         builder.append(entry.getAuthor() + " ,");
         log.info(entry.getAuthor().toString());
      }
      String authors = builder.toString();
      Project project = (Project) build.getProject();
      String displayName = project.getFullDisplayName();
      log.finest("Wall Notifier " + result);
      doNotification(DESCRIPTOR.getServerURLKey(), displayName, null, "Building", authors);
      return true;
   }

   @Override
   public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
      Result result = build.getResult();
      ChangeLogSet<? extends Entry> changeSet = build.getChangeSet();
      StringBuilder builder = new StringBuilder();
      boolean first = true;
      for (Entry entry : changeSet) {
         if (first) {
            first = false;
         } else {
            builder.append(" ,");
         }
         builder.append(entry.getAuthor());
      }
      String authors = builder.toString();
      Project project = (Project) build.getProject();
      String displayName = project.getFullDisplayName();
      log.info(displayName);
      if (result == Result.FAILURE) {
         log.finest("Wall Notifier " + displayName + " is a failure " + authors);
         doNotification(DESCRIPTOR.getServerURLKey(), displayName, null, "Failed", authors);
      } else if (result == Result.SUCCESS) {
         log.finest("Wall Notifier " + displayName + " is succesfull " + authors);
         doNotification(DESCRIPTOR.getServerURLKey(), displayName, null, "Success", authors);
      } else {
         log.warning("Wall Build: unhandled build result " + result);
      }
      return true;
   }

   public void doNotification(String serverKey, String project, String category, String status, String detail) {
      try {
         String projectEncoded = encode(project);
         String categoryEncoded = encode(category);
         String statusEncoded = encode(status);
         String detailEncoded = encode(detail);
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append(serverKey);
         stringBuilder.append("/publish");
         stringBuilder.append("?project=" + projectEncoded);
         stringBuilder.append("&status=" + statusEncoded);
         if (category != null) {
            stringBuilder.append("&category=" + categoryEncoded);
         }
         if (detailEncoded != null) {
            stringBuilder.append("&authors=" + detailEncoded);
         }
         String urlString = stringBuilder.toString();
         log.finest("now calling " + urlString);
         new URL(urlString).openStream();
      } catch (IOException e) {
         log.log(Level.SEVERE, e.getMessage());
      }
   }

   public String encode(String parameter) {
      if (parameter == null) {
         return null;
      }
      try {
         return URLEncoder.encode(parameter, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         log.log(Level.SEVERE, e.getMessage());
         return null;
      }
   }

}
