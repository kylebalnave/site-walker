/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.balnave.rambler;

import java.util.Collection;
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
        RamblerConfig config = new RamblerConfig("http://www.disney.co.uk/disney-channel/",
        "http://www\\.disney\\.co\\.uk/disney-channel/.*",
        ".*interstitial.*");
        config.setMaxLinkCount(2000);
        config.setMaxThreadCount(20);
        config.setStrictMemoryManagement(true);
        Rambler instance = new Rambler(config);
        Collection<RamblerResult> results = instance.getResults();
        String reportPath = "./test/channel-report.xml";
        boolean saved = RamblerReport.toXMLFile(config.getSiteUrl(), results, reportPath);
        System.out.println(String.format("Report saved to %s : %s", reportPath, saved));
    }
    
}
