package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author balnave
 */
public class Log extends AbstractReport {
    
    

    @Override
    public boolean out(Config config, Collection<Result> results, File fileOut) {
        for (Result result : getFailureResults(results)) {
            System.out.println(String.format("Failure %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
        for (Result result : getFailureResults(results)) {
            System.out.println(String.format("Error %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
        return false;
    }

}
