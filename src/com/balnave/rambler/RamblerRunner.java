package com.balnave.rambler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Loads a url and gets info
 *
 * @author balnave
 */
public class RamblerRunner implements Callable<RamblerResult> {

    private final String baseUrl;
    private RamblerResult result;
    private final boolean storeSourceStr;

    public RamblerRunner(String baseUrl, boolean storeSourceStr) {
        this.baseUrl = baseUrl;
        this.storeSourceStr = storeSourceStr;
    }

    @Override
    public RamblerResult call() throws Exception {
        URL urlToLoad;
        HttpURLConnection connection = null;
        try {
            urlToLoad = new URL(baseUrl);
            connection = (HttpURLConnection) urlToLoad.openConnection();
            result = new RamblerResult(connection, storeSourceStr);
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
        return result;
    }
    
    private void setErrorResult(HttpURLConnection connection, String msg) {
        try {
            result = new RamblerResult(baseUrl, connection.getResponseCode(), msg);
        } catch(IOException ex) {
            result = new RamblerResult(baseUrl, 0, msg);
        }
    }

    public RamblerResult getResult() {
        return result;
    }

}
