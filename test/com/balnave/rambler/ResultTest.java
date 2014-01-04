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
public class ResultTest extends TestCase {
    
    public ResultTest(String testName) {
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
     * Test of getResponseStatus method, of class Result.
     */
    public void testGetResponseStatus() {
        System.out.println("getResponseStatus");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        int expResult = 0;
        int result = instance.getResponseStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseMessage method, of class Result.
     */
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        String expResult = "hello world";
        String result = instance.getResponseMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReponseSource method, of class Result.
     */
    public void testGetReponseSource() {
        System.out.println("getReponseSource");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        String expResult = "";
        String result = instance.getReponseSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of setReponseSource method, of class Result.
     */
    public void testSetReponseSource() {
        System.out.println("setReponseSource");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        instance.setReponseSource("source");
        String expResult = "source";
        String result = instance.getReponseSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestUrl method, of class Result.
     */
    public void testGetRequestUrl() {
        System.out.println("getRequestUrl");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        instance.setReponseSource("source");
        String expResult = "http://www.google.com/";
        String result = instance.getRequestUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLinks method, of class Result.
     */
    public void testGetLinks() {
        System.out.println("getLinks");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        assertTrue(instance.getChildLinks() instanceof Collection);
    }

    /**
     * Test of clearLinks method, of class Result.
     */
    public void testClearLinks() {
        System.out.println("clearLinks");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        instance.setChildLinks(null);
        assertTrue(instance.getChildLinks() == null);
    }

    /**
     * Test of searchSourceForLinks method, of class Result.
     */
    public void testSearchSourceForLinks() throws Exception {
        System.out.println("searchSourceForLinks");
        Result instance = new Result("http://www.google.com/", 0, "hello world");
        String expResult = "http://www.google.com/apples-and-pears/index.jsp";
        Collection<String> result = instance.searchSourceForLinks("<a href='./apples-and-pears/index.jsp'><a href=\"./apples-and-pears/index2.jsp\">");
        assertEquals(2, result.size());
        assertEquals(expResult, result.toArray()[0]);
    }
    
}
