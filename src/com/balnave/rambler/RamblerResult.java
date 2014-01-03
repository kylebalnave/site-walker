package com.balnave.rambler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author balnave
 */
public final class RamblerResult {

    protected Collection<String> childLinks = new ArrayList<String>();
    protected int responseStatus = -1;
    protected String reponseSource = "";
    protected String responseMessage = "";
    protected String requestUrl = "";
    protected String parentUrl = "";
    
    public RamblerResult(String requestUrl, int responseStatus, String responseMessage) {
        this.parentUrl = requestUrl;
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }

    public RamblerResult(String parentUrl, String requestUrl, int responseStatus, String responseMessage) {
        this.parentUrl = parentUrl;
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }

    public RamblerResult(String parentUrl, HttpURLConnection connection) {
        this.parentUrl = parentUrl;
        this.requestUrl = connection.getURL().toExternalForm();
        connection.setConnectTimeout(1000 * 30);
        connection.setReadTimeout(1000 * 60);
        try {
            this.responseStatus = connection.getResponseCode();
            this.responseMessage = connection.getResponseMessage();
            if (connection.getContentType().contains("text/html")) {
                reponseSource = readInputStream(connection.getInputStream());
                childLinks = searchSourceForLinks(reponseSource);
            }
        } catch (IOException ioex) {
            this.responseStatus = 404;
            this.responseMessage = ioex.getMessage();
            this.reponseSource = "";
        } catch (URISyntaxException ex) {
            this.responseStatus = 0;
            this.responseMessage = "Cannot normalise child links! " + ex.getMessage();
            this.reponseSource = "";
        }
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getReponseSource() {
        return reponseSource;
    }

    public void setReponseSource(String source) {
        reponseSource = source;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Collection<String> getLinks() {
        return childLinks;
    }

    public String getParentUrl() {
        return parentUrl;
    }
    
    

    public void clearLinks() {
        childLinks.clear();
    }

    private String readInputStream(InputStream stream) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            return "";
        }
    }

    /**
     * Find all href attrs in the HTML source
     *
     * @param source
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    public List<String> searchSourceForLinks(String source) throws MalformedURLException, URISyntaxException {
        List<String> result = new ArrayList<String>();
        if (source == null) {
            return result;
        }
        Pattern p = Pattern.compile("href=\"(.*?)\"|href='(.*?)'");
        Matcher m = p.matcher(source);
        while (m.find()) {
            String href = m.group(1) == null ? m.group(2) : m.group(1);
            href = RamblerHelper.normaliseLink(requestUrl, href);
            if (RamblerHelper.isLinkingUrl(href) && !result.contains(href)) {
                result.add(href);
            }
        }
        return result;
    }
    
    public boolean isErrorResult() {
        int statusInt = this.getResponseStatus();
        String statusStr = String.valueOf(getResponseStatus());
        return statusInt <= 0 || statusStr.matches("50\\d{1}");
    }
    
    public boolean isFailureResult() {
        String statusStr = String.valueOf(getResponseStatus());
        return statusStr.matches("40\\d{1}");
    }
    
}
