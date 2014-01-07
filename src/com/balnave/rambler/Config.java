package com.balnave.rambler;

/**
 * Holds basic information required by a Rambler
 *
 * @author balnave
 */
public class Config {

    // the starting site url
    private final String siteUrl;
    // include and exclude RegExp aptterns
    private final String includesRegExp;
    private final String excludesRegExp;
    // 15 mins before the ramble stops
    private long timeoutMs = 1000 * 60 * 15;
    // thread limit
    private int maxThreadCount = 10;
    // returned link limit
    private int maxResultCount = 100;
    // additional data (will affect memory) 
    private boolean retainChildLinks = false;
    private boolean retainParentLinks = false;
    private boolean retainHtmlSource = false;
    // url authentication
    private String username = "";
    private String password = "";
    

    public Config(String siteUrl) {
        this(siteUrl, null, null);
    }
    

    public Config(String siteUrl, String includesRegExp) {
        this(siteUrl, includesRegExp, null);
    }

    public Config(String siteUrl, String includesRegExp, String excludesRegExp) {
        this.siteUrl = new URL(siteUrl).toString();
        this.includesRegExp = includesRegExp == null ? ".*" : includesRegExp;
        this.excludesRegExp = excludesRegExp == null ? "^\\s+$" : excludesRegExp;
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

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    public void setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public int getMaxResultCount() {
        return maxResultCount;
    }

    public void setMaxResultCount(int maxLinkCount) {
        this.maxResultCount = maxLinkCount;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public boolean isRetainChildLinks() {
        return retainChildLinks;
    }

    public void setRetainChildLinks(boolean retainChildLinks) {
        this.retainChildLinks = retainChildLinks;
    }

    public boolean isRetainParentLinks() {
        return retainParentLinks;
    }

    public void setRetainParentLinks(boolean retainParentLinks) {
        this.retainParentLinks = retainParentLinks;
    }

    public boolean isRetainHtmlSource() {
        return retainHtmlSource;
    }

    public void setRetainHtmlSource(boolean retainHtmlSource) {
        this.retainHtmlSource = retainHtmlSource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
