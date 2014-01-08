package com.balnave.rambler;

import com.balnave.rambler.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Result holds information obtained by the loaded Runner
 *
 * @author balnave
 */
public final class Result {

    private List<String> childLinks = new ArrayList<String>();
    private List<String> parentUrls = new ArrayList<String>();
    private String reponseSource = "";
    private String responseMessage = "";
    private String requestUrl = "";
    private String contentType = "";
    private int responseStatus = 0;
    private long loadDurationMs = 0;

    public Result(String requestUrl, int responseStatus, String responseMessage) {
        this(requestUrl, requestUrl, 0, responseMessage);
    }

    public Result(String parentUrl, String requestUrl, int responseStatus, String responseMessage) {
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.parentUrls.add(parentUrl);
    }

    public Result(String parentUrl, HttpURLConnection connection) {
        this.parentUrls.add(parentUrl);
        this.requestUrl = connection.getURL().toExternalForm();
        connection.setConnectTimeout(1000 * 30);
        connection.setReadTimeout(1000 * 60);
        try {
            this.responseStatus = connection.getResponseCode();
            this.responseMessage = connection.getResponseMessage();
            this.contentType = connection.getContentType();
            if (connection.getContentType().contains("text/html")) {
                reponseSource = readInputStream(connection.getInputStream());
                childLinks = searchSourceForLinks(reponseSource);
            }
        } catch (IOException ioex) {
            this.responseStatus = 404;
            this.responseMessage = ioex.getMessage();
            this.reponseSource = "";
        }
        Logger.log(String.format("Result: %s, %s", this.responseStatus, this.requestUrl), Logger.DEBUG);
    }

    public int getResponseStatus() {
        if (this.isErrorResult() || this.isFailureResult()) {
            // try to find status code in message
            Pattern p = Pattern.compile("\\s+(\\d+)\\s+");
            Matcher m = p.matcher(this.getResponseMessage());
            if (m.find()) {
                // assume the first number is the status
                return Integer.parseInt(m.group(1), 10);
            }
        }
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

    public List<String> getChildLinks() {
        return childLinks;
    }

    public void setChildLinks(List<String> childLinks) {
        this.childLinks = childLinks;
    }

    public List<String> getParentUrls() {
        return parentUrls;
    }

    public void setParentUrls(List<String> parentUrls) {
        this.parentUrls = parentUrls;
    }

    public void addParentUrl(String parentUrl) {
        this.parentUrls.add(parentUrl);
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
     */
    public List<String> searchSourceForLinks(String source) {
        List<String> result = new ArrayList<String>();
        if (source == null) {
            return result;
        }
        Pattern p = Pattern.compile("href=\"(.*?)\"|href='(.*?)'");
        Matcher m = p.matcher(source);
        while (m.find()) {
            String href = m.group(1) == null ? m.group(2) : m.group(1);
            boolean isAllowedType = !href.matches(".*\\.(ico|css|js)$");
            Logger.log(String.format("Result: %s found child href %s", URL.isValidURL(href), href), Logger.DEBUG);
            if (isAllowedType && URL.isValidURL(href)) {
                URL hrefUrl = new URL(requestUrl, href);
                String normalisedUrl = hrefUrl.toString();
                if (!result.contains(normalisedUrl)) {
                    result.add(normalisedUrl);
                    Logger.log(String.format("Result: found child link %s", normalisedUrl), Logger.DEBUG);
                } else {
                    Logger.log(String.format("Result: ignoring duplicate child link %s", normalisedUrl), Logger.DEBUG);
                }
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

    public String getContentType() {
        return contentType;
    }

    public long getLoadDurationMs() {
        return loadDurationMs;
    }

    public void setLoadDurationMs(long loadDurationMs) {
        this.loadDurationMs = loadDurationMs;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Result && (this.requestUrl.equalsIgnoreCase(((Result) obj).getRequestUrl()));

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.requestUrl != null ? this.requestUrl.hashCode() : 0);
        return hash;
    }

}
