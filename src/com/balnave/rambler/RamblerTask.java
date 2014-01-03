package com.balnave.rambler;

import java.util.Collection;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant wrapper to control the Rambler
 *
 * @author kyleb2
 */
public class RamblerTask extends Task {

    private String site;
    private String includes;
    private String excludes;
    private String maxThreads = String.valueOf(1);
    private String maxTimeout = String.valueOf(1000 * 16 * 15);
    private String maxLinks = String.valueOf(500);
    private String reportPath;

    // The method executing the task
    public void execute() throws BuildException {
        RamblerConfig config = new RamblerConfig(site, includes, excludes);
        config.setMaxLinkCount(Integer.valueOf(maxLinks));
        config.setMaxThreadCount(Integer.valueOf(maxThreads));
        config.setTimeoutMs(Integer.valueOf(maxTimeout));
        config.setStrictMemoryManagement(true);
        Rambler instance = null;
        try {
            instance = new Rambler(config);
        } catch (Exception ex) {
            throw new BuildException(String.format("Error running Rambler on site %s : %s", site, ex.getMessage()));
        }
        Collection<RamblerResult> results = instance.getResults();
        if (reportPath != null) {
            boolean saved = RamblerReport.toXMLFile(config.getSiteUrl(), results, reportPath);
            System.out.println(String.format("Report saved to %s : %s", reportPath, saved));
        } else {
            System.out.println(String.format("No Report saved... Links: %s Failures: %s Errors: %s",
                    results.size(),
                    RamblerReport.getFailureCount(results),
                    RamblerReport.getErrorCount(results)));
            if (RamblerReport.getFailureCount(results) > 0 || RamblerReport.getErrorCount(results) > 0) {
                for(RamblerResult result : RamblerReport.getFailureResults(results)) {
                    System.out.println(String.format("Failure %s %s %s", 
                            result.getResponseStatus(), 
                            result.getResponseMessage(), 
                            result.getRequestUrl()));
                }
                for(RamblerResult result : RamblerReport.getFailureResults(results)) {
                    System.out.println(String.format("Error %s %s %s",
                            result.getResponseStatus(), 
                            result.getResponseMessage(), 
                            result.getRequestUrl()));
                }
                throw new BuildException(String.format("One or more Failures or Errors 'Rambling' %s", site));
            }
        }
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public void setMaxThreads(String maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setMaxTimeout(String maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    public void setMaxLinks(String maxLinks) {
        this.maxLinks = maxLinks;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

}
