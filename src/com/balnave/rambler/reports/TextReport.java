package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Outputs the results to the System.out.println
 *
 * @author balnave
 */
public class TextReport extends SystemLog {

    public TextReport(Config config, List<Result> results) {
        super(config, results);
    }
    
    @Override
    protected String doForEachPassedResult(Result result) {
        return String.format("%s\n", result.getRequestUrl());
    }

    @Override
    protected String doForEachFailedResult(Result result) {
        return String.format("xx %s\n",
                result.getRequestUrl());
    }

    @Override
    protected String doForEachErrorResult(Result result) {
        return doForEachFailedResult(result);
    }


    @Override
    public boolean out(String fileOut) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.doForEachResultList(results));
        sb.append("\n=============\nRESULTS START\n=============\n\n");
        for(Result result : results) {
            if(result.isErrorResult()) {
                sb.append(this.doForEachErrorResult(result));
            } else if(result.isFailureResult()) {
                sb.append(this.doForEachFailedResult(result));
            } else {
                sb.append(this.doForEachPassedResult(result));
            }
        }
        sb.append("\n\n=============\nRESULTS END\n=============\n");
        try {
            PrintWriter out = new PrintWriter(fileOut, "UTF-8");
            out.print(sb.substring(0));
            out.close();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (UnsupportedEncodingException ex) {
            return false;
        }
        return true;
    }

}
