package com.balnave.rambler;

import com.balnave.rambler.logging.Logger;
import com.balnave.rambler.reports.Junit;
import com.balnave.rambler.reports.SystemLog;
import com.balnave.rambler.reports.XmlReport;
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
        Config config = new Config("http://www.disney.co.uk/disney-channel/features/maia-mitchell/", ".*", ".*");
        config.setMaxResultCount(20);
        config.setMaxThreadCount(20);
        config.setMaxAttempts(3);
        config.setRetainChildLinks(true);
        config.setRetainHtmlSource(true);
        config.setRetainParentLinks(true);
        Logger.setLevel(Logger.WARNING);
        Rambler instance = new Rambler(config);
        new SystemLog(config, instance.getResults()).out();
        new XmlReport(config, instance.getResults()).out("./test/rambler-report.xml");
        new Junit(config, instance.getResults()).out("./test/rambler-junit.xml");
    }

}
