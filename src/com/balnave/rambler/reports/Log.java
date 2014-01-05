package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.util.List;

/**
 * Outputs the results to the System.out.println
 * @author balnave
 */
public class Log extends AbstractReport {

    public Log(Config config, List<Result> results) {
        super(config, results);
    }
    
    
    @Override
    public void out() {
        for (Result result : getFailureResults()) {
            System.out.println(String.format("Failure %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
        for (Result result : getFailureResults()) {
            System.out.println(String.format("Error %s %s %s",
                    result.getResponseStatus(),
                    result.getResponseMessage(),
                    result.getRequestUrl()));
        }
    }

    @Override
    public boolean out(String fileOut) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
