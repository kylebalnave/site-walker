/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.balnave.rambler;

import junit.framework.TestCase;

/**
 *
 * @author kyleb2
 */
public class RamblerHelperTest extends TestCase {
    
    public RamblerHelperTest(String testName) {
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
     * Test of isFileUrl method, of class RamblerHelper.
     */
    public void testIsFileUrl() {
        System.out.println("testIsFileUrl");
        String url = "http://www.google.com/index.php";
        boolean expResult = true;
        boolean result = RamblerHelper.isFileUrl(url);
        assertEquals(expResult, result);
    }
    public void testIsNotFileUrl() {
        System.out.println("testIsNotFileUrl");
        String url = "http://www.google.com/";
        boolean expResult = false;
        boolean result = RamblerHelper.isFileUrl(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of needsTrailingSlash method, of class RamblerHelper.
     */
    public void testNeedsTrailingSlash() {
        System.out.println("needsTrailingSlash");
        String url = "http://www.google.com";
        boolean expResult = true;
        boolean result = RamblerHelper.needsTrailingSlash(url);
        assertEquals(expResult, result);
    }
    
    public void testDoesntNeedsTrailingSlash() {
        System.out.println("needsTrailingSlash");
        String url = "http://www.google.com/";
        boolean expResult = false;
        boolean result = RamblerHelper.needsTrailingSlash(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of addTrailingSlash method, of class RamblerHelper.
     */
    public void testAddTrailingSlash() {
        System.out.println("addTrailingSlash");
        String url = "http://www.google.com/";
        String expResult = "http://www.google.com/";
        String result = RamblerHelper.addTrailingSlash(url);
        assertEquals(expResult, result);
    }
    
    public void testAddTrailingSlashRequired() {
        System.out.println("addTrailingSlash");
        String url = "http://www.google.com";
        String expResult = "http://www.google.com/";
        String result = RamblerHelper.addTrailingSlash(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrlDomain method, of class RamblerHelper.
     */
    /*
    public void testGetUrlDomain() {
        System.out.println("getUrlDomain");
        String url = "";
        String expResult = "";
        String result = RamblerHelper.getUrlDomain(url);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    
}
