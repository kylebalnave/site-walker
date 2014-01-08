package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
     *
     * @param config
     * @param results
     */
    public AbstractReport(Config config, List<Result> results) {
        this.config = config;
        this.results = results;
        Collections.sort(this.results, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getResponseStatus() - o2.getResponseStatus();
            }
        });
    }

    /**
     * Out method
     */
    public abstract void out();

    /**
     * Out method
     *
     * @param fileOut
     * @return
     */
    public abstract boolean out(String fileOut);

    /**
     * Gets all the failure results
     *
     * @return
     */
    public List<Result> getFailureResults() {
        List<Result> failures = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isFailureResult()) {
                failures.add(result);
            }
        }
        return failures;
    }

    /**
     * Gets all the error results
     *
     * @return
     */
    public List<Result> getErrorResults() {
        List<Result> errors = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isErrorResult()) {
                errors.add(result);
            }
        }
        return errors;
    }

    /**
     * The number of Results
     *
     * @return
     */
    public int getResultsCount() {
        return results.size();
    }
    
    /**
     * Gets the total time taken for all tests
     * @return 
     */
    public long getResultsTimeMs() {
        long timeMs = 0;
        for(Result result : results) {
            timeMs += result.getLoadDurationMs();
        }
        return timeMs;
    }
    
    public String getTimeStamp() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy/MM/dd").format(date) + "T" + new SimpleDateFormat("h:mm:ss").format(date);
    }

    /**
     * The number of failure Results
     *
     * @return
     */
    public int getFailureCount() {
        return getFailureResults().size();
    }

    /**
     * The number of error Results
     *
     * @return
     */
    public int getErrorCount() {
        return getErrorResults().size();
    }

    /**
     * Action to perform for the full list of results
     *
     * @param results
     * @return
     */
    protected abstract Object doForEachResultList(List<Result> results);

    /**
     * Action to perform for each result
     *
     * @param result
     * @return
     */
    protected abstract Object doForEachPassedResult(Result result);

    /**
     * Action to perform for each failed result
     *
     * @param result
     * @return
     */
    protected abstract Object doForEachFailedResult(Result result);

    /**
     * Action to perform for each error result
     *
     * @param result
     * @return
     */
    protected abstract Object doForEachErrorResult(Result result);

}
