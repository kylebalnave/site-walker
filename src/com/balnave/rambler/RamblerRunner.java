package com.balnave.rambler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Loads a url
 *
 * @author balnave
 */
public class RamblerRunner implements Callable<RamblerResult> {

    private final String parentUrl;
    private final String baseUrl;
    private RamblerResult result;
    
    public RamblerRunner(String baseUrl) {
        this(baseUrl, baseUrl);
    }

    public RamblerRunner(String parentUrl, String baseUrl) {
        this.parentUrl = parentUrl;
        this.baseUrl = baseUrl;
    }

    @Override
    public RamblerResult call() throws Exception {
        URL urlToLoad;
        HttpURLConnection connection = null;
        try {
            urlToLoad = new URL(baseUrl);
            connection = (HttpURLConnection) urlToLoad.openConnection();
            result = new RamblerResult(parentUrl, connection);
        } catch (MalformedURLException ex) {
            setErrorResult(connection, ex.getMessage());
        } catch (IOException ex) {
            setErrorResult(connection, ex.getMessage());
        } catch (Exception ex) {
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
                result = new RamblerResult(parentUrl, baseUrl, connection.getResponseCode(), msg);
            } else {
                result = new RamblerResult(parentUrl, baseUrl, 0, msg);
            }
        } catch (IOException ex) {
            result = new RamblerResult(parentUrl, baseUrl, 0, msg);
        }
    }

    public RamblerResult getResult() {
        return result;
    }

}
