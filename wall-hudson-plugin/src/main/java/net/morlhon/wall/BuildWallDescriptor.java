package net.morlhon.wall;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

public class BuildWallDescriptor extends BuildStepDescriptor<Publisher> {

   private String serverURLKey;

   public BuildWallDescriptor() {
      super(BuildWallPublisher.class);
      load();
   }

   @Override
   public boolean configure(final StaplerRequest req, JSONObject json) throws FormException {
      serverURLKey = req.getParameter("serverURLKey");
      save();
      return super.configure(req, json);
   }

   @Override
   public boolean isApplicable(Class<? extends AbstractProject> jobType) {
      return true;
   }

   @Override
   public String getDisplayName() {
      return "Build Wall Publisher";
   }

   public void setServerURLKey(String serverURLKey) {
      this.serverURLKey = serverURLKey;
   }

   public String getServerURLKey() {
      return serverURLKey;
   }

}
