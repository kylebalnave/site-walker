package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author balnave
 */
public abstract class AbstractReport {
    
    protected final Config config;
    protected final List<Result> results;

    /**
     * Constructor
     * @param config
     * @param results
     */
    public AbstractReport(Config config, List<Result> results) {
        this.config = config;
        this.results = results;
    }
    
    public abstract void out();

    public abstract boolean out(String fileOut);

    public List<Result> getFailureResults() {
        List<Result> failures = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isFailureResult()) {
                failures.add(result);
            }
        }
        return failures;
    }

    public List<Result> getErrorResults() {
        List<Result> errors = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isErrorResult()) {
                errors.add(result);
            }
        }
        return errors;
    }

    public int getFailureCount() {
        return getFailureResults().size();
    }

    public int getErrorCount() {
        return getErrorResults().size();
    }

    protected Comparator sortResponseStatus = new Comparator<Result>() {

        @Override
        public int compare(Result o1, Result o2) {
            return o1.getResponseStatus() - o2.getResponseStatus();
        }
    };

}
