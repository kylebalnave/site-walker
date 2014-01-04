package com.balnave.rambler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Creates multiple Runner Classes to scrape a website
 *
 * @author balnave
 */
public class Rambler {

    private final Collection<Result> results = new ArrayList<Result>();
    private final Collection<String[]> queuedUrls = new ArrayList<String[]>();
    private final Config config;

    public Rambler(Config config) throws Exception {
        long startTimeMs = System.currentTimeMillis();
        this.config = config;
        //
        // add the first url to the queue
        queuedUrls.add(createQueueItem(config.getSiteUrl(), config.getSiteUrl()));
        //
        // loop through each queued url and load
        ExecutorService execSvc = Executors.newFixedThreadPool(config.getMaxThreadCount());
        while (canContinueWalkingSiteUrls(startTimeMs)) {
            //
            // create multiple threads to load the urls
            Collection<Runner> callables = loadQueuedUrls();
            execSvc.invokeAll(callables, 30, TimeUnit.SECONDS);
            //
            // loop throw the threads and get any child links to queue
            for (Runner runner : callables) {
                Result result = runner.getResult();
                if (result != null && !results.contains(result)) {
                    results.add(result);
                    queueChildLinks(result);
                }
                if (result != null && config.isStrictMemoryManagement()) {
                    result.setChildLinks(null);
                    result.setReponseSource(null);
                }
            }
            callables.clear();
            //
            // check if the timeout has been exceeded
            long timeSinceStartMs = System.currentTimeMillis() - startTimeMs;
            if (timeSinceStartMs >= config.getTimeoutMs()) {
                throw new TimeoutException(String.format("Timeout of %sms caused Rambler to stop!", config.getTimeoutMs()));
            } else {
                //
                // sleep before another loop to allow other processes
                Thread.sleep(250);
            }

        }
        System.out.println(String.format("Site %s finished!", config.getSiteUrl()));
    }

    /**
     * Gets all the child links from a result object ands adds to a queue if
     * needed
     *
     * @param result
     * @throws Exception
     */
    private void queueChildLinks(Result result) throws Exception {
        if (result != null && result.getChildLinks() != null) {
            for (String tagHref : result.getChildLinks()) {
                boolean matchesIncludedUrl = config.getIncludesRegExp() == null || tagHref.matches(config.getIncludesRegExp());
                boolean matchesExcludedUrl = config.getIncludesRegExp() != null && tagHref.matches(config.getExcludesRegExp());
                if (matchesIncludedUrl && !matchesExcludedUrl) {
                    Result storedResult = getResultWithUrl(tagHref);
                    boolean urlIsAlreadyLoaded = storedResult != null;
                    String[] queueItem = createQueueItem(result.getRequestUrl(), tagHref);
                    boolean isQueued = queueContains(queueItem[1]);
                    if (urlIsAlreadyLoaded && !config.isStrictMemoryManagement()) {
                        getResultWithUrl(tagHref).addParentUrl(result.getRequestUrl());
                    } else if (!urlIsAlreadyLoaded && !isQueued) {
                        queuedUrls.add(createQueueItem(result.getRequestUrl(), tagHref));
                    }
                }
            }
        }
    }

    /**
     * Checks if the queue contains a url
     *
     * @param url
     * @return
     */
    private boolean queueContains(String url) {
        for (String[] item : queuedUrls) {
            if (item[1].equalsIgnoreCase(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks is a result already exists with a url
     *
     * @param url
     * @return
     */
    private Result getResultWithUrl(String url) {
        for (Result includedResult : results) {
            if (includedResult != null && includedResult.getRequestUrl().equals(url)) {
                return includedResult;
            }
        }
        return null;
    }

    /**
     *
     * @param execSvc
     * @param maxThreads
     * @return
     */
    private Collection<Runner> loadQueuedUrls() {
        Collection<Runner> callables = new ArrayList<Runner>();
        int threadIndex = 0;
        //
        // create multiple threads to load urls
        while (threadIndex < config.getMaxThreadCount() && !queuedUrls.isEmpty()) {
            threadIndex++;
            String[] queuedUrl = ((List<String[]>) queuedUrls).get(0);
            queuedUrls.remove(queuedUrl);
            callables.add(new Runner(queuedUrl[0], queuedUrl[1]));
        }
        return callables;
    }

    /**
     * Checks if the site walking can continue
     *
     * @param startTimeMs
     * @return
     */
    private boolean canContinueWalkingSiteUrls(long startTimeMs) {
        //
        // check if the timeout has been exceeded
        long timeSinceStartMs = System.currentTimeMillis() - startTimeMs;
        //
        // debug status
        System.out.println(String.format("Queue: %s, Complete: %s, Elapsed Time: %s:%s",
                queuedUrls.size(),
                results.size(),
                TimeUnit.MILLISECONDS.toMinutes(timeSinceStartMs),
                TimeUnit.MILLISECONDS.toSeconds(timeSinceStartMs) - (60 * TimeUnit.MILLISECONDS.toMinutes(timeSinceStartMs))));
        return queuedUrls.size() > 0 && results.size() < config.getMaxLinkCount();
    }

    private String[] createQueueItem(String parentUrl, String childUrl) {
        String[] item;
        item = new String[2];
        item[0] = parentUrl;
        item[1] = childUrl;
        return item;
    }

    public Collection<Result> getResults() {
        return results;
    }
}
