package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author balnave
 */
public abstract class AbstractReport {

    public boolean out(Config config, Collection<Result> results) {
        return out(config, results, "");
    }

    public boolean out(Config config, Collection<Result> results, String fileOut) {
        return out(config, results, fileOut.isEmpty() ? null : new File(fileOut));
    }

    public abstract boolean out(Config config, Collection<Result> results, File fileOut);

    public static Collection<Result> getFailureResults(Collection<Result> results) {
        Collection<Result> failures = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isFailureResult()) {
                failures.add(result);
            }
        }
        return failures;
    }

    public static Collection<Result> getErrorResults(Collection<Result> results) {
        Collection<Result> errors = new ArrayList<Result>();
        for (Result result : results) {
            if (result.isErrorResult()) {
                errors.add(result);
            }
        }
        return errors;
    }

    public static int getFailureCount(Collection<Result> results) {
        return getFailureResults(results).size();
    }

    public static int getErrorCount(Collection<Result> results) {
        return getErrorResults(results).size();
    }

    protected Comparator sortResponseStatus = new Comparator<Result>() {

        @Override
        public int compare(Result o1, Result o2) {
            return o1.getResponseStatus() - o2.getResponseStatus();
        }
    };

}
