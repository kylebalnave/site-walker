package com.balnave.rambler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author balnave
 */
public final class RamblerResult {

    protected List<String> normalisedLinks = new ArrayList<String>();
    protected int responseStatus = -1;
    protected String reponseSource = "";
    protected String responseMessage = "";
    protected String requestUrl = "";
    protected String requestDomain = "";

    public RamblerResult(String requestUrl, int responseStatus, String responseMessage) {
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.requestDomain = getUrlDomain(requestUrl);
    }

    public RamblerResult(HttpURLConnection connection) {
        this(connection, true);
    }

    public RamblerResult(HttpURLConnection connection, boolean storeSourceStr) {
        this.requestUrl = connection.getURL().toExternalForm();
        connection.setConnectTimeout(1000 * 30);
        connection.setReadTimeout(1000 * 60);
        try {
            BufferedReader bufferedReader;
            StringBuilder sb = new StringBuilder();
            String line;
            this.responseStatus = connection.getResponseCode();
            this.responseMessage = connection.getResponseMessage();
            if (connection.getContentType().contains("text/html")) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                reponseSource = sb.toString();
                List<String> sourceLinks = searchSourceForLinks((String) reponseSource);
                for (String href : sourceLinks) {
                    try {
                        if (href instanceof String && !href.matches("^(javascript|#).*$")) {
                            String normalisedHref = normaliseLink(requestUrl, href);
                            normalisedLinks.add(normalisedHref);
                        }
                    } catch (URISyntaxException r) {
                        // skip href
                    } finally {
                        if (!storeSourceStr) {
                            reponseSource = "";
                        }
                    }
                }
            }
        } catch (IOException ioex) {
            this.responseStatus = 404;
            this.responseMessage = ioex.getMessage();
            this.reponseSource = "";
        }
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Object getReponseSource() {
        return reponseSource;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public List<String> getLinks() {
        return normalisedLinks;
    }

    public void clearLinks() {
        normalisedLinks.clear();
    }

    /**
     * Find all href attrs in the HTML source
     *
     * @param source
     * @return
     */
    public List<String> searchSourceForLinks(String source) {
        List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("href=\"(.*?)\"|href='(.*?)'");
        Matcher m = p.matcher(source);
        while (m.find()) {
            result.add(m.group(1));
        }
        return result;
    }

    /**
     * Normalises a url link
     *
     * @param baseUrl
     * @param href
     * @return
     * @throws java.net.URISyntaxException
     * @throws java.net.MalformedURLException
     */
    public String normaliseLink(String baseUrl, String href) throws URISyntaxException, MalformedURLException {
        URI baseURI = new URI(baseUrl);
        if (href == null || href.matches("^(http|https|www|ftp).*$")) {
            // href is absolute
            return href;
        } else if (!baseURI.isAbsolute()) {
            throw new MalformedURLException(String.format("The baseUrl must be absolute, but got %s", baseUrl));
        } else if (href.startsWith("/")) {
            href = requestDomain + href;
        }
        URI hrefURI = new URI(href);
        URI normalisedURI = baseURI.resolve(hrefURI);
        return normalisedURI.toString().split("#")[0];
    }

    public String getUrlDomain(String url) {
        String domainName = url;
        String scheme = "";
        int index = domainName.indexOf("://");
        if (index != -1) {
            // keep everything after the "://"
            scheme = url.substring(0, index + 3);
            domainName = url.substring(index + 3);
        }
        index = domainName.indexOf('/');
        if (index != -1) {
            // keep everything before the '/'
            domainName = domainName.substring(0, index);
        }
        return scheme + domainName;
    }

    public void setReponseSource(String reponseSource) {
        this.reponseSource = reponseSource;
    }

    public List<String> getNormalisedLinks() {
        return normalisedLinks;
    }

    public void setNormalisedLinks(List<String> normalisedLinks) {
        this.normalisedLinks = normalisedLinks;
    }

    public String getRequestDomain() {
        return requestDomain;
    }

    public void setRequestDomain(String requestDomain) {
        this.requestDomain = requestDomain;
    }

}
