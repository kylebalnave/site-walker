package com.balnave.rambler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper utils
 *
 * @author kyleb2
 */
public class RamblerHelper {

    private static String getLastUrlDirPart(String url) {
        int lastSlashIndex = url.lastIndexOf("/");
        return lastSlashIndex > 0 ? url.substring(lastSlashIndex + 1) : url;
    }

    public static boolean isFileUrl(String url) {
        return getLastUrlDirPart(url).split("\\.").length == 2;
    }

    public static boolean needsTrailingSlash(String url) {
        if (!isFileUrl(url)) {
            return !getLastUrlDirPart(url).isEmpty();
        }
        return false;
    }

    public static String addTrailingSlash(String url) {
        if (needsTrailingSlash(url)) {
            return url + "/";
        }
        return url;
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
    public static String normaliseLink(String baseUrl, String href) throws URISyntaxException, MalformedURLException {
        URI baseURI = new URI(baseUrl);
        if (href == null || href.matches("^(http|https|www|ftp).*$")) {
            // href is absolute
            return href;
        } else if (!baseURI.isAbsolute()) {
            throw new MalformedURLException(String.format("The baseUrl must be absolute, but got %s", baseUrl));
        } else if (href.startsWith("/")) {
            href = getUrlDomain(baseUrl) + href;
        }
        URI hrefURI = new URI(href);
        URI normalisedURI = baseURI.resolve(hrefURI);
        return normalisedURI.toString().split("#")[0];
    }

    public static String getUrlDomain(String url) {
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

    /**
     * Is a url linking i.e. its not a # value or javascript: call
     *
     * @param url
     * @return
     */
    public static boolean isLinkingUrl(String url) {
        return url instanceof String && !url.matches("^(javascript|#).*$");
    }

    public static String urlToFilePath(String url) {
        boolean isFile = isFileUrl(url);
        String lastPart = getLastUrlDirPart(url);
        url = url.replaceAll("http:\\/\\/|https:\\/\\/|ftp:\\/\\/|\\?|=|&", "");
        url = url.replace(".", "_");
        url = url.replace(lastPart, isFile ? lastPart.split("\\.")[0] : lastPart);
        url = url.replace("/", File.separator);
        //try {
            return url;
            //return URLEncoder.encode(url, "UTF-8").replace("%5C", "/");
        //} catch (UnsupportedEncodingException ex) {
           // return url;
        //}
    }
}
