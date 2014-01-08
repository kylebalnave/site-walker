package com.balnave.rambler.ant;

import com.balnave.io.GZip;
import com.balnave.rambler.Config;
import com.balnave.rambler.Rambler;
import com.balnave.rambler.Result;
import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.reports.Junit;
import com.balnave.rambler.reports.SystemLog;
import com.balnave.rambler.reports.XmlReport;
import java.io.File;
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
    private String maxAttempts = String.valueOf(3);
    private String reportsDir = "";
    private String reportType = "";
    private String gzip = "false";
    private String loggingLevel = "-1";
    private String username = "";
    private String password = "";

    /**
     * The method executing the task
     *
     * @throws BuildException
     */
    @Override
    public void execute() throws BuildException {
        boolean hasReportsDir = !reportsDir.isEmpty();
        boolean needsFullReport = hasReportsDir && reportType.contains("full");
        boolean needsFullOrLinksReport = hasReportsDir && (needsFullReport || reportType.contains("links"));
        boolean needsJUnitReport = hasReportsDir && reportType.contains("junit");
        boolean needsLogReport = reportType.contains("log");
        boolean gZipReports = Boolean.valueOf(gzip);
        Logger.setLevel(Integer.valueOf(loggingLevel));
        Config config = new Config(site, includes, excludes);
        config.setMaxResultCount(Integer.valueOf(maxLinks));
        config.setMaxThreadCount(Integer.valueOf(maxThreads));
        config.setMaxAttempts(Integer.valueOf(maxAttempts));
        config.setTimeoutMs(Integer.valueOf(maxTimeout));
        config.setRetainChildLinks(needsFullOrLinksReport);
        config.setRetainHtmlSource(needsFullReport);
        config.setRetainParentLinks(needsFullOrLinksReport);
        config.setUsername(username);
        config.setPassword(password);
        Rambler instance = null;
        try {
            instance = new Rambler(config);
        } catch (Exception ex) {
            throw new BuildException(String.format("Error running Rambler on site %s : %s", site, ex.getMessage()));
        }
        List<Result> results = instance.getResults();
        String reportOutPath;
        if (needsFullOrLinksReport) {
            reportOutPath = reportsDir + "/rambler-report.xml";
            boolean saved = new XmlReport(config, results).out(reportOutPath);
            Logger.log(String.format("Summary report saved to %s : %s", reportOutPath, saved), Logger.DEBUG);
            if (saved && gZipReports) {
                GZip.zip(reportOutPath, reportOutPath + ".gzip", true);
            }
        }
        if (needsJUnitReport) {
            reportOutPath = reportsDir + "/rambler-junit.xml";
            boolean saved = new Junit(config, results).out(reportOutPath);
            Logger.log(String.format("JUnit report saved to %s : %s", reportOutPath, saved), Logger.DEBUG);
            if (saved && gZipReports) {
                GZip.zip(reportOutPath, reportOutPath + ".gzip", true);
            }
        }
        if (needsLogReport) {
            new SystemLog(config, results).out();
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

    public void setReportsDir(String reportsDir) {
        File outDir = new File(reportsDir);
        if (!outDir.isFile()) {
            if (!outDir.exists()) {
                boolean success = outDir.mkdir();
                if (!success) {
                    throw new BuildException(String.format("Cannot create reportsDir: %s", outDir.getAbsolutePath()));
                }
            }
        } else {
            throw new BuildException(String.format("reportsDir must be a directory: %s", outDir.getAbsolutePath()));
        }
        this.reportsDir = outDir.getAbsolutePath();
    }

    public void setLoggingLevel(String level) {
        this.loggingLevel = level;
    }

    public void setGzip(String gzip) {
        this.gzip = gzip;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType.toLowerCase();
    }

    public void setMaxAttempts(String maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    
    

}
