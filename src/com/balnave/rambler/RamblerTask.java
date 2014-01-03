package com.balnave.rambler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String sourcePath;
    private String verbose;

    // The method executing the task
    public void execute() throws BuildException {
        RamblerConfig config = new RamblerConfig(site, includes, excludes);
        config.setMaxLinkCount(Integer.valueOf(maxLinks));
        config.setMaxThreadCount(Integer.valueOf(maxThreads));
        config.setTimeoutMs(Integer.valueOf(maxTimeout));
        config.setStrictMemoryManagement(sourcePath == null);
        Rambler instance = null;
        try {
            instance = new Rambler(config);
        } catch (Exception ex) {
            throw new BuildException(String.format("Error running Rambler on site %s : %s", site, ex.getMessage()));
        }
        Collection<RamblerResult> results = instance.getResults();
        if (sourcePath != null) {
            System.out.println(String.format("Saving sources to %s", sourcePath));
            for (RamblerResult result : results) {
                String srcPath = RamblerHelper.urlToFilePath(RamblerHelper.addTrailingSlash(sourcePath) + result.getRequestUrl()) + "src.xml";
                try {
                    File dir = new File(srcPath);
                    dir.createNewFile();
                    dir.setWritable(true);
                    /*
                    FileWriter fw = new FileWriter(dir);
                    fw.write(result.getReponseSource());
                    fw.close();
                            */
                } catch (FileNotFoundException ex) {
                    System.out.println(String.format("Cannot save source to %s : %s", srcPath, ex.getMessage()));
                } catch (IOException ex) {
                    System.out.println(String.format("Cannot save source to %s : %s", srcPath, ex.getMessage()));
                }
            }
        }
        if (reportPath != null) {
            boolean saved = RamblerReport.toXMLFile(config.getSiteUrl(), results, reportPath);
            System.out.println(String.format("JUnit report saved to %s : %s", reportPath, saved));
        } else {
            if (RamblerReport.getFailureCount(results) > 0 || RamblerReport.getErrorCount(results) > 0) {
                if (Boolean.valueOf(verbose)) {
                    for (RamblerResult result : RamblerReport.getFailureResults(results)) {
                        System.out.println(String.format("Failure %s %s %s",
                                result.getResponseStatus(),
                                result.getResponseMessage(),
                                result.getRequestUrl()));
                    }
                    for (RamblerResult result : RamblerReport.getFailureResults(results)) {
                        System.out.println(String.format("Error %s %s %s",
                                result.getResponseStatus(),
                                result.getResponseMessage(),
                                result.getRequestUrl()));
                    }
                }
                System.out.println(String.format("No Report saved... Links: %s Failures: %s Errors: %s",
                        results.size(),
                        RamblerReport.getFailureCount(results),
                        RamblerReport.getErrorCount(results)));
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

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }

}
