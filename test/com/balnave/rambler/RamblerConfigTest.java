package com.balnave.rambler;

import java.net.MalformedURLException;
import junit.framework.TestCase;

/**
 *
 * @author kyleb2
 */
public class RamblerConfigTest extends TestCase {
    
    public RamblerConfigTest(String testName) {
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
     * Test of getSiteUrl method, of class RamblerConfig.
     */
    public void testGetSiteUrlWithoutTrailingSlash() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithoutTrailingSlash");
        RamblerConfig instance = new RamblerConfig("http://www.google.com");
        String expResult = "http://www.google.com/";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }
    
    public void testGetSiteUrlWithTrailingSlash() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithTrailingSlash");
        RamblerConfig instance = new RamblerConfig("http://www.google.com/");
        String expResult = "http://www.google.com/";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }
    
    public void testGetSiteUrlWithFile() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithFile");
        RamblerConfig instance = new RamblerConfig("http://www.google.com/index.php");
        String expResult = "http://www.google.com/index.php";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIncludesRegExp method, of class RamblerConfig.
     */
    public void testGetIncludesRegExp() throws MalformedURLException {
        System.out.println("getIncludesRegExp");
        RamblerConfig instance = new RamblerConfig("www.google.com/", "^apples$");
        String expResult = "^apples$";
        String result = instance.getIncludesRegExp();
        assertEquals(expResult, result);
    }
    
    public void testGetIncludesNullRegExp() throws MalformedURLException {
        System.out.println("getIncludesRegExp");
        RamblerConfig instance = new RamblerConfig("www.google.com/");
        String expResult = null;
        String result = instance.getIncludesRegExp();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExcludesRegExp method, of class RamblerConfig.
     */
    public void testGetExcludesRegExp() throws MalformedURLException {
        System.out.println("getExcludesRegExp");
        RamblerConfig instance = new RamblerConfig("www.google.com/", "^apples$", "^pears$");
        String expResult = "^pears$";
        String result = instance.getExcludesRegExp();
        assertEquals(expResult, result);
    }
    
    public void testGetExcludesNullRegExp() throws MalformedURLException {
        System.out.println("getExcludesRegExp");
        RamblerConfig instance = new RamblerConfig("www.google.com/", "^apples$");
        String expResult = null;
        String result = instance.getExcludesRegExp();
        assertEquals(expResult, result);
    }
    
}
