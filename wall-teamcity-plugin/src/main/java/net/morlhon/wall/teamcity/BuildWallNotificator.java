package net.morlhon.wall.teamcity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserSet;
import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.SelectPrevBuildPolicy;

public class BuildWallNotificator implements Notificator {
   private static final String TYPE = "buildWallNotifier";
   private static final String TYPE_NAME = "Build Wall Notifier";
   private static final String SERVER_URL_ID = "wallServerURL";

   private static final PropertyKey SERVER_URL_KEY = new NotificatorPropertyKey(TYPE, SERVER_URL_ID);

   public BuildWallNotificator(NotificatorRegistry notificatorRegistry) throws IOException {
      System.err.println("#BWN# Registering BuildWallNotificator");
      ArrayList<UserPropertyInfo> userProps = new ArrayList<UserPropertyInfo>();
      userProps.add(new UserPropertyInfo(SERVER_URL_ID, "Build Wall Server URL"));
      notificatorRegistry.register(this, userProps);
   }

   private void doNotification(Set<SUser> sUsers, String project, String status, String detail) {
      for (SUser user : sUsers) {
         String projectName = null;
         String category = null;
         int indexOf = project.indexOf("::");
         if (indexOf == -1) {
            projectName = project;
            category = null;
         } else {
            category = project.substring(0, indexOf).trim();
            projectName = project.substring(indexOf + 2).trim();
         }
         try {
            String projectEncoded = URLEncoder.encode(projectName, "UTF-8");
            String categoryEncoded = URLEncoder.encode(category, "UTF-8");
            String statusEncoded = URLEncoder.encode(status, "UTF-8");
            String detailEncoded = URLEncoder.encode(detail, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(user.getPropertyValue(SERVER_URL_KEY));
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
            System.out.println("now calling " + urlString);
            new URL(urlString).openStream();
         } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
         } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
         } catch (IOException e) {
            System.err.println(e.getMessage());
         }
      }
   }

   public void notifyBuildSuccessful(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      doNotification(sUsers, sRunningBuild.getFullName(), "Success", buildDetailString(sRunningBuild));
   }

   public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      doNotification(sUsers, sRunningBuild.getFullName(), "Failed", buildDetailString(sRunningBuild));
   }

   private String buildDetailString(SRunningBuild runningBuild) {
      String committers = buildCommitterString(runningBuild);
      if ((committers != null) && (committers.length() > 0)) {
         System.err.println("#BW# Sending out the committer list since it's not empty : " + committers);
         return committers;
      }
      String changes = buildChangesList(runningBuild);
      if ((changes != null) && (changes.length() > 0)) {
         System.err.println("#BW# Sending out the changes list since it's not empty : " + changes);
         return changes;
      }
      String dependencies = buildDependencyString(runningBuild);
      System.err.println("#BW# Sending out the dependency list : " + dependencies);
      return dependencies;
   }

   private String buildChangesList(SRunningBuild sRunningBuild) {
      StringBuilder stringBuilder = new StringBuilder();
      List<SVcsModification> changes = sRunningBuild.getChanges(SelectPrevBuildPolicy.SINCE_LAST_BUILD, true);
      boolean first = true;
      for(SVcsModification modification : changes) {
         if (first) {
            first = false;
         } else {
            stringBuilder.append(", ");
         }
         stringBuilder.append(modification.getUserName());
      }
      return stringBuilder.toString();
   }
   
   private String buildCommitterString(SRunningBuild sRunningBuild) {
      UserSet<SUser> userSet = sRunningBuild.getCommitters(SelectPrevBuildPolicy.SINCE_LAST_SUCCESSFULLY_FINISHED_BUILD);
      System.err.println("#BWN# SINCE_LAST_SUCCESS =>" + printOutUserSet(userSet));
      userSet = sRunningBuild.getCommitters(SelectPrevBuildPolicy.SINCE_FIRST_BUILD);
      System.err.println("#BWN# SINCE_FIRST_BUILD =>" + printOutUserSet(userSet));
      userSet = sRunningBuild.getCommitters(SelectPrevBuildPolicy.SINCE_LAST_BUILD);
      System.err.println("#BWN# SINCE_LAST_BUILD =>" + printOutUserSet(userSet));
      StringBuffer userString = new StringBuffer();
      String finalUserString = new String();
      if ((userSet != null) && (userSet.getUsers() != null) && (!userSet.getUsers().isEmpty())) {
         for (SUser user : userSet.getUsers()) {
            userString.append(user.getUsername());
            userString.append(", ");
         }
         finalUserString = userString.substring(0, userString.length() - 2);
      }
      System.err.println("#BWN# CommitterString =>" + finalUserString);
      return finalUserString;
   }

   private String printOutUserSet(UserSet<SUser> userSet) {
      if (userSet == null) {
         return "null userset";
      }
      if (userSet.getUsers() == null) {
         return "null userset.GetUsers";
      }
      if (userSet.getUsers().size() == 0) {
         return "userSet.getUsers().size()==0";
      }
      StringBuilder builder = new StringBuilder();
      builder.append("size"+userSet.getUsers().size());
      for (SUser user : userSet.getUsers()) {
         builder.append(user.getUsername());
         builder.append(", ");
      }
      return builder.toString();
   }

   private String buildDependencyString(SRunningBuild runningBuild) {
      if (runningBuild.getTriggeredBy() == null) {
         return "";
      }
      System.err.println("#BWN# raw=>" + runningBuild.getTriggeredBy().getRawTriggeredBy());
      if (runningBuild.getTriggeredBy().isTriggeredByUser()) {
         System.err.println("#BWN# Build triggered by User");
         return runningBuild.getTriggeredBy().getAsString();
      }
      System.err.println("#BWN# Build triggered by something else");
      Map<String, String> parameters = runningBuild.getTriggeredBy().getParameters();
      Set<String> keySet = parameters.keySet();
      for (String key : keySet) {
         System.err.println(key + "=>" + parameters.get(key));
      }
      String result = runningBuild.getTriggeredBy().getAsString();
      int indexOf = result.indexOf("::");
      if (indexOf == -1) {
         return result;
      }
      return result.substring(indexOf + 2);
   }

   public void notifyLabelingFailed(Build build, jetbrains.buildServer.vcs.VcsRoot vcsRoot, Throwable throwable, Set<SUser> sUsers) {
      // Do nothing
   }

   public void notifyBuildFailing(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      // Do Nothing
   }

   public void notifyBuildProbablyHanging(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      // Do Nothing
   }

   public void notifyResponsibleChanged(SBuildType sBuildType, Set<SUser> sUsers) {
      // Do Nothing
   }

   public void notifyBuildStarted(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      doNotification(sUsers, sRunningBuild.getFullName(), "Building", buildDetailString(sRunningBuild));
   }

   public String getNotificatorType() {
      return TYPE;
   }

   public String getDisplayName() {
      return TYPE_NAME;
   }

}
