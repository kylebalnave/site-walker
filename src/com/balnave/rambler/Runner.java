package com.balnave.rambler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Loads a url and creates a Result
 *
 * @author balnave
 */
public class Runner implements Callable<Result> {

    private final String parentUrl;
    private final String baseUrl;
    private Result result;
    
    public Runner(String baseUrl) {
        this(baseUrl, baseUrl);
    }

    public Runner(String parentUrl, String baseUrl) {
        this.parentUrl = parentUrl;
        this.baseUrl = baseUrl;
    }

    @Override
    public Result call() {
        URL urlToLoad;
        HttpURLConnection connection = null;
        try {
            urlToLoad = new URL(baseUrl);
            connection = (HttpURLConnection) urlToLoad.openConnection();
            result = new Result(parentUrl, connection);
        } catch (Exception ex) {
            // keep a genral exception here to catch all!
            setErrorResult(connection, ex.getMessage());
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (result == null) {
            setErrorResult(null, String.format("Unknown error loading url %s", baseUrl));
        }
        return result;
    }

    private void setErrorResult(HttpURLConnection connection, String msg) {
        try {
            if (connection != null) {
                result = new Result(parentUrl, baseUrl, connection.getResponseCode(), msg);
            } else {
                result = new Result(parentUrl, baseUrl, 0, msg);
            }
        } catch (IOException ex) {
            result = new Result(parentUrl, baseUrl, 0, msg);
        }
    }

    public Result getResult() {
        return result;
    }

}
