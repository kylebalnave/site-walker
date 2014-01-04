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
public class RunnerTest extends TestCase {

    public RunnerTest(String testName) {
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
     * Test of call method, of class Runner.
     */
    public void testCall() throws Exception {
        System.out.println("call");
        Runner instance = new Runner("http://www.google.com/");
        int expResult = 200;
        Result result = instance.call();
        assertEquals(expResult, result.getResponseStatus());
    }

    /**
     * Test of getResult method, of class Runner.
     */
    public void testGetResult() throws Exception {
        System.out.println("getResult");
        Runner instance = new Runner("http://www.google.com/");
        Result result = instance.call();
        Result expResult = instance.getResult();
        assertEquals(expResult, result);
    }

}
