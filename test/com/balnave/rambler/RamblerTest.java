package com.balnave.rambler;

import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.reports.Junit;
import com.balnave.rambler.reports.Log;
import com.balnave.rambler.reports.Summary;
import junit.framework.TestCase;

/**
 *
 * @author kyleb2
 */
public class RamblerTest extends TestCase {
    
    public RamblerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getResults method, of class Rambler.
     */
    public void testGetResults() throws Exception {
        Config config = new Config("http://www.disney.co.uk/disney-channel/", ".*http:\\/\\/www.disney.co.uk\\/disney-channel\\/.*", ".*interstitial.*");
        config.setMaxResultCount(2000);
        config.setMaxThreadCount(50);
        config.setRetainChildLinks(true);
        config.setRetainHtmlSource(true);
        config.setRetainParentLinks(true);
        Logger.setLevel(Logger.WARNING);
        Rambler instance = new Rambler(config);
        new Junit(config, instance.getResults()).out("./test/channel-summary.xml");
    }
    
}
