package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import com.balnave.rambler.logging.Logger;
import java.util.List;

/**
 * Outputs the results to the System.out.println
 *
 * @author balnave
 */
public class SystemLog extends AbstractReport {

    public SystemLog(Config config, List<Result> results) {
        super(config, results);
    }

    @Override
    protected String doForEachResultList(List<Result> results) {
        return String.format("Site:'%s'\nIncludes Pattern:'%s'\nExcludes Pattern:'%s'\nTests(%s) -- Failures(%s) -- Errors(%s)\n",
                config.getSiteUrl(),
                config.getIncludesRegExp(),
                config.getExcludesRegExp(),
                getResultsCount(),
                getFailureCount(),
                getErrorCount());
    }

    @Override
    protected String doForEachPassedResult(Result result) {
        return String.format("Pass Code:'%s' -- Url:'%s'\n",
                result.getResponseStatus(),
                result.getRequestUrl());
    }

    @Override
    protected String doForEachFailedResult(Result result) {
        return String.format("Failure Code:'%s' -- Url:'%s'\nMessage:'%s'\n",
                result.getResponseStatus(),
                result.getRequestUrl(),
                result.getResponseMessage());
    }

    @Override
    protected String doForEachErrorResult(Result result) {
        return String.format("Error Code:'%s' -- Url:'%s'\nMessage:'%s'\n",
                result.getResponseStatus(),
                result.getRequestUrl(),
                result.getResponseMessage());
    }

    @Override
    public void out() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=============\nRESULTS START\n=============\n\n");
        sb.append(this.doForEachResultList(results));
        for(Result result : results) {
            if(result.isErrorResult()) {
                sb.append(this.doForEachErrorResult(result));
            } else if(result.isFailureResult()) {
                sb.append(this.doForEachFailedResult(result));
            } else {
                Logger.log(this.doForEachPassedResult(result), Logger.DEBUG);
            }
        }
        sb.append("\n\n=============\nRESULTS END\n=============\n");
        Logger.log(sb.toString(), Logger.ALLWAYS);
    }

    @Override
    public boolean out(String fileOut) {
        out();
        return false;
    }

}
