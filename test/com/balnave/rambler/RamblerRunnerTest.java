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
public class RamblerRunnerTest extends TestCase {

    public RamblerRunnerTest(String testName) {
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
     * Test of call method, of class RamblerRunner.
     */
    public void testCall() throws Exception {
        System.out.println("call");
        RamblerRunner instance = new RamblerRunner("http://www.google.com/");
        int expResult = 200;
        RamblerResult result = instance.call();
        assertEquals(expResult, result.getResponseStatus());
    }

    /**
     * Test of getResult method, of class RamblerRunner.
     */
    public void testGetResult() throws Exception {
        System.out.println("getResult");
        RamblerRunner instance = new RamblerRunner("http://www.google.com/");
        RamblerResult result = instance.call();
        RamblerResult expResult = instance.getResult();
        assertEquals(expResult, result);
    }

}
