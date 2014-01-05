package com.balnave.rambler.queue;

import com.balnave.rambler.URL;

/**
 *
 * @author balnave
 */
public class QueueItem {

    private URL parentUrl;
    private URL itemUrl;
    
    public QueueItem(String itemUrlStr) {
        this(itemUrlStr, itemUrlStr);
    }

    public QueueItem(String parentUrlStr, String itemUrlStr) {
        parentUrl = new URL(parentUrlStr);
        itemUrl = new URL(parentUrlStr, itemUrlStr);
    }
    

    /**
     * Get the value of itemUrl
     *
     * @return the value of itemUrl
     */
    public URL getItemUrl() {
        return itemUrl;
    }

    /**
     * Set the value of itemUrl
     *
     * @param itemUrl new value of itemUrl
     */
    public void setItemUrl(URL itemUrl) {
        this.itemUrl = itemUrl;
    }

    /**
     * Get the value of parentUrl
     *
     * @return the value of parentUrl
     */
    public URL getParentUrl() {
        return parentUrl;
    }

    /**
     * Set the value of parentUrl
     *
     * @param parentUrl new value of parentUrl
     */
    public void setParentUrl(URL parentUrl) {
        this.parentUrl = parentUrl;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.itemUrl.toString() != null ? this.itemUrl.toString().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ((QueueItem)obj).getItemUrl() == null ||this.itemUrl == null) {
            return false;
        }
        return ((QueueItem)obj).getItemUrl().toString().equalsIgnoreCase(this.itemUrl.toString());
    }
    
    

}
