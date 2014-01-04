package com.balnave.rambler.reports;

import com.balnave.rambler.RamblerConfig;
import com.balnave.rambler.RamblerResult;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author balnave
 */
public class Log extends AbstractReport {
    
    

    @Override
    public boolean out(RamblerConfig config, Collection<RamblerResult> results, File fileOut) {
        for (RamblerResult result : getFailureResults(results)) {
            System.out.println(String.format("Failure %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
        for (RamblerResult result : getFailureResults(results)) {
            System.out.println(String.format("Error %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
        return false;
    }

}
