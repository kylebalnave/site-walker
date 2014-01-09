package com.balnave.rambler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Used to normalise a url a get other criteria
 *
 * @author balnave
 */
public final class URL {

    protected String urlStr;

    public URL(String url) {
        this(url, url);
    }

    public URL(String baseUrl, String url) {
        urlStr = normaliseLink(baseUrl, url);
    }

    public static boolean isValidURL(String url) {
        if(url.startsWith("#") || url.startsWith("javascript:")) {
            return false;
        }
        return createURI(url) != null;
    }

    /**
     * Creates a URI and catches exceptions
     *
     * @param url
     * @return
     */
    private static URI createURI(String url) {
        if(url == null) {
            return null;
        }
        try {
            return new URI(url);
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    private static java.net.URL createURL(String url) {
        if(url == null) {
            return null;
        }
        try {
            return new java.net.URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    private String getLastUrlDirPart(String url) {
        int lastSlashIndex = url.lastIndexOf("/");
        return lastSlashIndex > 0 ? url.substring(lastSlashIndex + 1) : url;
    }

    private boolean isFileUrl(String url) {
        return getLastUrlDirPart(url).split("\\.").length == 2;
    }

    private boolean needsTrailingSlash(String url) {
        if (!isFileUrl(url)) {
            return !getLastUrlDirPart(url).isEmpty();
        }
        return false;
    }

    private String addTrailingSlash(String url) {
        if (needsTrailingSlash(url)) {
            return url + "/";
        }
        return url;
    }

    /**
     * Removes everything except the start domain part of a url
     *
     * @param url
     * @return
     */
    protected String getUrlDomain(String url) {
        String domainName = url;
        String scheme;
        int index = domainName.indexOf("://");
        if (index == -1) {
            // create a scheme
            scheme = "http://";
        } else {
            // keep everything after the "://"
            scheme = url.substring(0, index + 3);
            domainName = url.substring(index + 3);
        }
        index = domainName.indexOf('/');
        if (index != -1) {
            // keep everything before the '/'
            domainName = domainName.substring(0, index + 1);
        }
        if (!domainName.endsWith("/")) {
            domainName = domainName + "/";
        }
        return scheme + domainName;
    }

    /**
     * Resolves relative urls and removes the # fragment
     *
     * @param baseUrl
     * @param url
     * @return
     */
    protected String normaliseLink(String url) {
        return normaliseLink(url, url);
    }
    protected String normaliseLink(String baseUrl, String url) {
        baseUrl = isValidURL(baseUrl) ? addTrailingSlash(baseUrl) : baseUrl;
        //url = isValidURL(url) ? addTrailingSlash(url) : url;
        URI baseUri = createURI(baseUrl);
        if (baseUri != null && baseUri.getScheme() == null) {
            // add http:// scheme to the base url
            baseUri = createURI("http://" + baseUrl);
        }
        URI relUri = createURI(url);
        if (baseUri == null || relUri == null || relUri.getScheme() != null) {
            // invalid url or absolute
            return url.split("#")[0];
        } else if (url.startsWith("//")) {
            // use base url as server root
            url = (baseUrl.startsWith("https://") ? "https://" : "http://") + url.substring(2);
        } else if (url.startsWith("/")) {
            // use base url as server root
            url = getUrlDomain(baseUrl) + url.substring(1);
        }
        URI absUri = createURI(url.split("#")[0]);
        URI normalisedUri = baseUri.resolve(absUri);
        return normalisedUri.toASCIIString();
    }

    @Override
    public String toString() {
        return urlStr;
    }

}
