package com.balnave.rambler.reports;

import com.balnave.rambler.RamblerConfig;
import com.balnave.rambler.RamblerResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author balnave
 */
public abstract class AbstractReport {

    public boolean out(RamblerConfig config, Collection<RamblerResult> results) {
        return out(config, results, "");
    }

    public boolean out(RamblerConfig config, Collection<RamblerResult> results, String fileOut) {
        return out(config, results, fileOut.isEmpty() ? null : new File(fileOut));
    }

    public abstract boolean out(RamblerConfig config, Collection<RamblerResult> results, File fileOut);

    public static Collection<RamblerResult> getFailureResults(Collection<RamblerResult> results) {
        Collection<RamblerResult> failures = new ArrayList<RamblerResult>();
        for (RamblerResult result : results) {
            if (result.isFailureResult()) {
                failures.add(result);
            }
        }
        return failures;
    }

    public static Collection<RamblerResult> getErrorResults(Collection<RamblerResult> results) {
        Collection<RamblerResult> errors = new ArrayList<RamblerResult>();
        for (RamblerResult result : results) {
            if (result.isErrorResult()) {
                errors.add(result);
            }
        }
        return errors;
    }

    public static int getFailureCount(Collection<RamblerResult> results) {
        return getFailureResults(results).size();
    }

    public static int getErrorCount(Collection<RamblerResult> results) {
        return getErrorResults(results).size();
    }

    protected Comparator sortResponseStatus = new Comparator<RamblerResult>() {

        @Override
        public int compare(RamblerResult o1, RamblerResult o2) {
            return o1.getResponseStatus() - o2.getResponseStatus();
        }
    };

}
