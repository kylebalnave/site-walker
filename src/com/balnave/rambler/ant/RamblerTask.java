package com.balnave.rambler.ant;

import com.balnave.io.GZip;
import com.balnave.rambler.Config;
import com.balnave.rambler.Rambler;
import com.balnave.rambler.Result;
import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.reports.AbstractReport;
import com.balnave.rambler.reports.Junit;
import com.balnave.rambler.reports.Log;
import com.balnave.rambler.reports.Summary;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant wrapper to control the Rambler
 *
 * @author kyleb2
 */
public class RamblerTask extends Task {

    private String site = "";
    private String includes = "^\\s+$";
    private String excludes = "^\\s+$";
    private String maxThreads = String.valueOf(1);
    private String maxTimeout = String.valueOf(1000 * 60 * 15);
    private String maxLinks = String.valueOf(500);
    private String reportPath = "";
    private String summaryPath = "";
    private String gzip = "false";
    private String loggingLevel = "-1";

    /**
     * The method executing the task
     * @throws BuildException
     */
        @Override
    public void execute() throws BuildException {
        Logger.setLevel(Integer.valueOf(loggingLevel));
        Config config = new Config(site, includes, excludes);
        config.setMaxResultCount(Integer.valueOf(maxLinks));
        config.setMaxThreadCount(Integer.valueOf(maxThreads));
        config.setTimeoutMs(Integer.valueOf(maxTimeout));
        config.setRetainChildLinks(!summaryPath.isEmpty());
        config.setRetainHtmlSource(Boolean.valueOf(gzip));
        config.setRetainParentLinks(!summaryPath.isEmpty());
        Rambler instance = null;
        boolean gZipReports = Boolean.valueOf(gzip);
        try {
            instance = new Rambler(config);
        } catch (Exception ex) {
            throw new BuildException(String.format("Error running Rambler on site %s : %s", site, ex.getMessage()));
        }
        List<Result> results = instance.getResults();
        
        if (!summaryPath.isEmpty()) {
            boolean saved = new Summary(config, results).out(summaryPath);
            Logger.log(String.format("Summary report saved to %s : %s", summaryPath, saved),Logger.DEBUG);
            if(saved && gZipReports) {
                GZip.zip(summaryPath, summaryPath + ".gzip", true);
            }
        } 
        if (!reportPath.isEmpty()) {
            boolean saved = new Junit(config, results).out(reportPath);
            Logger.log(String.format("JUnit report saved to %s : %s", reportPath, saved), Logger.DEBUG);
        }
        if (reportPath.isEmpty() && summaryPath.isEmpty()) {
            AbstractReport report = new Log(config, results);
            if (report.getFailureCount() > 0 || report.getErrorCount() > 0) {
                Logger.log(String.format("No Report saved... Links: %s Failures: %s Errors: %s",
                        results.size(),
                        report.getFailureCount(),
                        report.getErrorCount()), Logger.DEBUG);
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

    public void setSummaryPath(String summaryPath) {
        this.summaryPath = summaryPath;
    }

    public void setLoggingLevel(String level) {
        this.loggingLevel = level;
    }

    public void setGZip(String gzip) {
        this.gzip = gzip;
    }
    
    

}
