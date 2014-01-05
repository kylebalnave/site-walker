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
    
    public Runner(QueueItem itemToLoad) {
        this.itemToLoad = itemToLoad;
    }

    @Override
    public Result call() {
        URL urlToLoad;
        String urlStr = itemToLoad.getItemUrl().toString();
        HttpURLConnection connection = null;
        try {
            urlToLoad = new URL(urlStr);
            Logger.log(String.format("Runner: loading %s", urlStr), Logger.DEBUG);
            connection = (HttpURLConnection) urlToLoad.openConnection();
            result = new Result(itemToLoad.getParentUrl().toString(), connection);
        } catch (Exception ex) {
            // keep a genral exception here to catch all!
            Logger.log(String.format("Runner Exception: %s %s", urlStr, ex.getMessage()), Logger.WARNING);
            setErrorResult(connection, ex.getMessage());
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (result == null) {
            Logger.log(String.format("Runner Exception: %s %s", urlStr, "Result is NULL!"), Logger.WARNING);
            setErrorResult(null, String.format("Unknown error loading url %s", urlStr));
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
