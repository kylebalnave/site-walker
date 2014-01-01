package com.balnave.rambler;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Creates multiple RamblerRunner Classes to scrape a website
 *
 * @author balnave
 */
public class RamblerThreadFactory {

    public static long timeoutMs = 1000 * 60 * 15; // 15 mins
    public static int urlLimit = 100;
    public static int maxThreads = 50;
    public static boolean keepResponseChildLinks = false;
    public static boolean keepResponseSource = false;
    //
    private final HashSet<RamblerResult> results = new HashSet<RamblerResult>();
    private final HashSet<String> queuedUrls = new HashSet<String>();
    private final String included_urls_regexp;
    private final String excluded_urls_regexp;

    public RamblerThreadFactory(RamblerConfig config) throws Exception {
        long startTimeMs = System.currentTimeMillis();
        this.included_urls_regexp = config.getIncludesRegExp();
        this.excluded_urls_regexp = config.getExcludesRegExp();
        //
        // add the first url to the queue
        queuedUrls.add(config.getSiteUrl());
        //
        // loop through each queued url and load
        ExecutorService execSvc = Executors.newFixedThreadPool(maxThreads);
        while (canContinueWalkingSiteUrls(startTimeMs)) {
            Collection<RamblerRunner> callables = loadQueuedUrls();
            execSvc.invokeAll(callables, 30, TimeUnit.SECONDS);
            //
            // loop throw the threads and get any child links to queue
            for (RamblerRunner runner : callables) {
                RamblerResult result = runner.getResult();
                if (!results.contains(result)) {
                    results.add(result);
                    queueChildLinks(result);
                    // reset the child links list to save memory
                    if (!keepResponseChildLinks) {
                        result.clearLinks();
                    }
                }
            }
            callables.clear();
            //
            // debug status
            System.out.println(String.format("Queue: %s, Urls: %s", queuedUrls.size(), results.size()));
            //
            // sleep before another loop to allow other processes
            Thread.sleep(250);
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
    private void queueChildLinks(RamblerResult result) throws Exception {
        //
        // follow the other hrefs from here
        if (result != null && result.getLinks() instanceof Collection) {
            for (String tagHref : result.getLinks()) {
                boolean urlIsAlreadyLoaded = false;
                for (RamblerResult includedResult : results) {
                    if (includedResult.getRequestUrl().equals(tagHref)) {
                        urlIsAlreadyLoaded = true;
                        break;
                    }
                }
                if (!urlIsAlreadyLoaded) {
                    boolean matchesIncludedUrl = tagHref.matches(included_urls_regexp);
                    boolean matchesExcludedUrl = tagHref.matches(excluded_urls_regexp);
                    if (matchesIncludedUrl && !matchesExcludedUrl) {
                        queuedUrls.add(tagHref);
                    } else {
                        results.add(new RamblerResult(tagHref, 0, "URL excluded by RegExp!"));
                    }
                }
            }
        }
    }

    /**
     *
     * @param execSvc
     * @param maxThreads
     * @return
     */
    private Collection<RamblerRunner> loadQueuedUrls() {
        Collection<RamblerRunner> callables = new ArrayList<RamblerRunner>();
        int threadIndex = 0;
            //
        // create multiple threads to load urls
        while (threadIndex < maxThreads && queuedUrls.iterator().hasNext()) {
            threadIndex++;
            String queuedUrl = (String) queuedUrls.iterator().next();
            queuedUrls.remove(queuedUrl);
            callables.add(new RamblerRunner(queuedUrl, keepResponseSource));
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
        if (timeSinceStartMs >= timeoutMs) {
            System.out.println("Timeout caused cancel command!");
            return false;
        }
        return queuedUrls.size() > 0 && results.size() < urlLimit;
    }
    
    public Collection<RamblerResult> getResults() {
        return results;
    }
}
