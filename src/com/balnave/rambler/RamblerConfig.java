package com.balnave.rambler;


import java.net.MalformedURLException;

/**
 * The config for a site walker
 * @author balnave
 */
public class RamblerConfig {
    
    private final String siteUrl;
    private final String includesRegExp;
    private final String excludesRegExp;

    public RamblerConfig(String siteUrl, String includesRegExp, String excludesRegExp) throws MalformedURLException {
        if (!siteUrl.endsWith("/")) {
            throw new MalformedURLException(String.format("The site URL should end with a '/', but got %s", siteUrl));
        }
        this.siteUrl = siteUrl;
        this.includesRegExp = includesRegExp;
        this.excludesRegExp = excludesRegExp;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getIncludesRegExp() {
        return includesRegExp;
    }

    public String getExcludesRegExp() {
        return excludesRegExp;
    }

    
    
}
