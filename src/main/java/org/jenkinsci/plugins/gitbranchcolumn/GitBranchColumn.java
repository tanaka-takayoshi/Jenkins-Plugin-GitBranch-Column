package org.jenkinsci.plugins.gitbranchcolumn;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.git.BranchSpec;
import hudson.tasks.Builder;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCM;
import java.util.List;
/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link GitBranchColumn} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author tanaka_733
 */
public class GitBranchColumn extends ListViewColumn  {

    @DataBoundConstructor
    public GitBranchColumn() {
            super();
    }

  
    public String formatBranchName(@SuppressWarnings("rawtypes") Job job) {
        if (job instanceof Project) {
            Project project = (Project) job;
            SCM scm = project.getScm();
            if (scm instanceof GitSCM) {
                GitSCM git = (GitSCM)scm;
                List<BranchSpec> branches = git.getBranches();
                if (branches != null && branches.size() > 0) {
                    BranchSpec branch = branches.get(0);
                    String branchName = branch.getName();
                    String[] splits = branchName.split("/");
                    if (splits.length > 0) {
                        return splits[splits.length - 1];
                    }
                    
                }
            }
        }
        return "";
    }

    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {

        @Override
        public boolean shownByDefault() {
                return true;
        }

        @Override
        public String getDisplayName() {
                return "GitBranchColumn";
        }
    }
}

