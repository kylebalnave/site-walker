package com.balnave.rambler;

import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.queue.QueueItem;
import com.balnave.rambler.queue.Queue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Creates multiple Threaded Runner Classes to scrape a web site's links
 *
 * @author balnave
 */
public class Rambler {

    private final List<Result> results = new ArrayList<Result>();
    private final Queue queue = new Queue();
    private final Config config;
    private boolean forceStop = false;

    public Rambler(Config config) throws Exception {
        long startTimeMs = System.currentTimeMillis();
        this.config = config;
        //
        // add the first url to the queue
        queue.add(config.getSiteUrl());
        //
        // loop through each queued url and load
        ExecutorService execSvc = Executors.newFixedThreadPool(config.getMaxThreadCount());
        while (!queue.isEmpty() && results.size() < config.getMaxResultCount() && forceStop == false) {
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
                if (result != null) {
                    if (!config.isRetainChildLinks()) {
                        result.setChildLinks(new ArrayList<String>());
                    }
                    if (!config.isRetainHtmlSource()) {
                        result.setReponseSource("");
                    }
                }
            }
            callables.clear();
            //
            // check if the timeout has been exceeded
            long timeSinceStartMs = System.currentTimeMillis() - startTimeMs;
            //
            // debug status
            Logger.log(String.format("Rambler: Queue: %s, Complete: %s, Elapsed Time: %s:%s", 
                    queue.size(),
                    results.size(),
                    TimeUnit.MILLISECONDS.toMinutes(timeSinceStartMs),
                    TimeUnit.MILLISECONDS.toSeconds(timeSinceStartMs) - (60 * TimeUnit.MILLISECONDS.toMinutes(timeSinceStartMs))), Logger.ALLWAYS);

            if (timeSinceStartMs >= config.getTimeoutMs()) {
                throw new TimeoutException(String.format("Timeout of %sms caused Rambler to stop!", config.getTimeoutMs()));
            } else {
                //
                // sleep before another loop to allow other processes
                Thread.sleep(250);
            }

        }
        Logger.log(String.format("Site %s finished rambling!", config.getSiteUrl()), Logger.ALLWAYS);
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
            Logger.log(String.format("Rambler: child link count %s", result.getChildLinks().size()), Logger.DEBUG);
            for (String tagHref : result.getChildLinks()) {
                
                boolean matchesIncludedUrl = tagHref.matches(config.getIncludesRegExp());
                Logger.log(String.format("Rambler: checking child url %s", tagHref), Logger.DEBUG);
                Logger.log(String.format("Rambler: child matches includes patterns %s: %s", config.getIncludesRegExp(), matchesIncludedUrl), Logger.DEBUG);
                boolean matchesExcludedUrl = tagHref.matches(config.getExcludesRegExp());
                Logger.log(String.format("Rambler: child matches excludes patterns %s: %s", config.getExcludesRegExp(), matchesExcludedUrl), Logger.DEBUG);
                if (matchesIncludedUrl && !matchesExcludedUrl) {
                    Result storedResult = getResultWithUrl(tagHref);
                    boolean resultExists = storedResult != null;
                    boolean isQueued = queue.contains(tagHref);
                    Logger.log(String.format("Rambler: child link loaded (%s), queued (%s)", resultExists, isQueued), Logger.DEBUG);
                    if (resultExists && config.isRetainParentLinks()) {
                        getResultWithUrl(tagHref).addParentUrl(result.getRequestUrl());
                    } else if (!resultExists && !isQueued) {
                        queue.add(result.getRequestUrl(), tagHref);
                    }
                }
            }
        }
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
     * Load multiple urls using Threads
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
        while (threadIndex < config.getMaxThreadCount() && !queue.isEmpty()) {
            QueueItem queueItem = queue.removeFirst();
            callables.add(new Runner(queueItem));
            threadIndex++;
        }
        return callables;
    }

    /**
     * Returns the Results as a List
     *
     * @return
     */
    public List<Result> getResults() {
        return results;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }
    
    
}
