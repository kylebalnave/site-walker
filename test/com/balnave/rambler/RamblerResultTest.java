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
public class RamblerResultTest extends TestCase {
    
    public RamblerResultTest(String testName) {
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
     * Test of getResponseStatus method, of class RamblerResult.
     */
    public void testGetResponseStatus() {
        System.out.println("getResponseStatus");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        int expResult = 0;
        int result = instance.getResponseStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseMessage method, of class RamblerResult.
     */
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        String expResult = "hello world";
        String result = instance.getResponseMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReponseSource method, of class RamblerResult.
     */
    public void testGetReponseSource() {
        System.out.println("getReponseSource");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        String expResult = "";
        String result = instance.getReponseSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of setReponseSource method, of class RamblerResult.
     */
    public void testSetReponseSource() {
        System.out.println("setReponseSource");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        instance.setReponseSource("source");
        String expResult = "source";
        String result = instance.getReponseSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestUrl method, of class RamblerResult.
     */
    public void testGetRequestUrl() {
        System.out.println("getRequestUrl");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        instance.setReponseSource("source");
        String expResult = "http://www.google.com/";
        String result = instance.getRequestUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLinks method, of class RamblerResult.
     */
    public void testGetLinks() {
        System.out.println("getLinks");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        assertTrue(instance.getLinks() instanceof Collection);
    }

    /**
     * Test of clearLinks method, of class RamblerResult.
     */
    public void testClearLinks() {
        System.out.println("clearLinks");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        instance.clearLinks();
        assertTrue(instance.getLinks() instanceof Collection);
    }

    /**
     * Test of searchSourceForLinks method, of class RamblerResult.
     */
    public void testSearchSourceForLinks() throws Exception {
        System.out.println("searchSourceForLinks");
        RamblerResult instance = new RamblerResult("http://www.google.com/", 0, "hello world");
        String expResult = "http://www.google.com/apples-and-pears/index.jsp";
        Collection<String> result = instance.searchSourceForLinks("<a href='./apples-and-pears/index.jsp'><a href=\"./apples-and-pears/index2.jsp\">");
        assertEquals(2, result.size());
        assertEquals(expResult, result.toArray()[0]);
    }
    
}
