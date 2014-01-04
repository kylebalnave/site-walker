package com.balnave.rambler;

/**
 * The config for a site walker
 * Holds basic information required to find all site links
 * @author balnave
 */
public class Config {
    
    private final String siteUrl;
    private final String includesRegExp;
    private final String excludesRegExp;
    private long timeoutMs = 1000 * 60 * 15; // 15 mins before the ramble stops
    private int maxThreadCount = 10;
    private int maxLinkCount = 100;
    private boolean strictMemoryManagement = false; // deletes child links and source string to reduce memory
    
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

    public int getMaxLinkCount() {
        return maxLinkCount;
    }

    public void setMaxLinkCount(int maxLinkCount) {
        this.maxLinkCount = maxLinkCount;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public boolean isStrictMemoryManagement() {
        return strictMemoryManagement;
    }

    public void setStrictMemoryManagement(boolean strictMemoryManagement) {
        this.strictMemoryManagement = strictMemoryManagement;
    }

    
    
}
