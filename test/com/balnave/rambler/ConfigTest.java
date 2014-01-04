package com.balnave.rambler;

import java.net.MalformedURLException;
import junit.framework.TestCase;

/**
 *
 * @author kyleb2
 */
public class ConfigTest extends TestCase {
    
    public ConfigTest(String testName) {
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
     * Test of getSiteUrl method, of class Config.
     */
    public void testGetSiteUrlWithoutTrailingSlash() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithoutTrailingSlash");
        Config instance = new Config("http://www.google.com");
        String expResult = "http://www.google.com/";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }
    
    public void testGetSiteUrlWithTrailingSlash() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithTrailingSlash");
        Config instance = new Config("http://www.google.com/");
        String expResult = "http://www.google.com/";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }
    
    public void testGetSiteUrlWithFile() throws MalformedURLException {
        System.out.println("testGetSiteUrlWithFile");
        Config instance = new Config("http://www.google.com/index.php");
        String expResult = "http://www.google.com/index.php";
        String result = instance.getSiteUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIncludesRegExp method, of class Config.
     */
    public void testGetIncludesRegExp() throws MalformedURLException {
        System.out.println("getIncludesRegExp");
        Config instance = new Config("www.google.com/", "^apples$");
        String expResult = "^apples$";
        String result = instance.getIncludesRegExp();
        assertEquals(expResult, result);
    }
    
    public void testGetIncludesNullRegExp() throws MalformedURLException {
        System.out.println("getIncludesRegExp");
        Config instance = new Config("www.google.com/");
        String expResult = null;
        String result = instance.getIncludesRegExp();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExcludesRegExp method, of class Config.
     */
    public void testGetExcludesRegExp() throws MalformedURLException {
        System.out.println("getExcludesRegExp");
        Config instance = new Config("www.google.com/", "^apples$", "^pears$");
        String expResult = "^pears$";
        String result = instance.getExcludesRegExp();
        assertEquals(expResult, result);
    }
    
    public void testGetExcludesNullRegExp() throws MalformedURLException {
        System.out.println("getExcludesRegExp");
        Config instance = new Config("www.google.com/", "^apples$");
        String expResult = null;
        String result = instance.getExcludesRegExp();
        assertEquals(expResult, result);
    }
    
}
