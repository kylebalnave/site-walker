package com.balnave.rambler;

import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.queue.QueueItem;
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

    private final QueueItem itemToLoad;
    private Result result;
    private int maxAttempts = 3;

    public Runner(QueueItem itemToLoad) {
        this.itemToLoad = itemToLoad;
    }
    
    public Runner(QueueItem itemToLoad, int maxAttempts) {
        this.itemToLoad = itemToLoad;
        this.maxAttempts = maxAttempts;
    }

    private Result connect(String urlStr) {
        URL urlToLoad;
        HttpURLConnection connection = null;
        Result connectionResult = null;
        try {
            urlToLoad = new URL(urlStr);
            Logger.log(String.format("Runner: loading %s", urlStr), Logger.DEBUG);
            long startMs = System.currentTimeMillis();
            connection = (HttpURLConnection) urlToLoad.openConnection();
            connectionResult = new Result(itemToLoad.getParentUrl().toString(), connection);
            connectionResult.setLoadDurationMs(System.currentTimeMillis() - startMs);
        } catch (Exception ex) {
            // keep a genral exception here to catch all!
            Logger.log(String.format("Runner Exception: %s %s", urlStr, ex.getMessage()), Logger.WARNING);
            setErrorResult(connection, ex.getMessage());
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (connectionResult == null) {
            Logger.log(String.format("Runner Exception: %s %s", urlStr, "Result is NULL!"), Logger.WARNING);
            setErrorResult(null, String.format("Unknown error loading url %s", urlStr));
        }
        return connectionResult;
    }

    @Override
    public Result call() {
        String urlStr = itemToLoad.getItemUrl().toString();
        int attempts = 0;
        while (attempts < maxAttempts) {
            attempts++;
            result = connect(urlStr);
            if (result.isErrorResult() || result.isFailureResult()) {
                try {
                    // wait 3 secs to try to connect again
                    Logger.log(String.format("Runner attempt %s of %s, sleep for 3 secs before retrying %s", attempts, maxAttempts, urlStr), Logger.DEBUG);
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException ex) {
                    Logger.log("Error sleeping Runner thread: " + ex.getMessage(), Logger.WARNING);
                }
            } else {
                break;
            }
        }
        return result;
    }

    private void setErrorResult(HttpURLConnection connection, String msg) {
        try {
            if (connection != null) {
                result = new Result(itemToLoad.getParentUrl().toString(), itemToLoad.getItemUrl().toString(), connection.getResponseCode(), msg);
            } else {
                result = new Result(itemToLoad.getParentUrl().toString(), itemToLoad.getItemUrl().toString(), 0, msg);
            }
        } catch (IOException ex) {
            result = new Result(itemToLoad.getParentUrl().toString(), itemToLoad.getItemUrl().toString(), 0, msg);
        }
    }

    public Result getResult() {
        return result;
    }

}
