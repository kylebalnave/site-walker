/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.balnave.rambler;

import java.net.MalformedURLException;
import junit.framework.TestCase;

/**
 *
 * @author balnave
 */
public class URLTest extends TestCase {
    
    public URLTest(String testName) {
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
     * Test of getUrlDomain method, of class URL.
     * @throws java.net.MalformedURLException
     */
    public void testGetUrlDomainAbsoluteFileUrl() throws MalformedURLException {
        System.out.println("testGetUrlDomainAbsoluteFileUrl");
        String url = "http://www.google.co.uk/path/file.php";
        URL instance = new URL(url);
        String expResult = "http://www.google.co.uk/";
        String result = instance.getUrlDomain(url);
        assertEquals(expResult, result);
    }
    public void testGetUrlDomainAbsoluteDirUrl() throws MalformedURLException {
        System.out.println("testGetUrlDomainAbsoluteDirUrl");
        String url = "http://www.google.co.uk/path/";
        URL instance = new URL(url);
        String expResult = "http://www.google.co.uk/";
        String result = instance.getUrlDomain(url);
        assertEquals(expResult, result);
    }
    public void testGetUrlDomainAbsoluteShortUrlWithSlash() throws MalformedURLException {
        System.out.println("testGetUrlDomainAbsoluteShortUrlWithSlash");
        String url = "http://www.google.co.uk/";
        URL instance = new URL(url);
        String expResult = "http://www.google.co.uk/";
        String result = instance.getUrlDomain(url);
        assertEquals(expResult, result);
    }
    public void testGetUrlDomainAbsoluteShortUrlWithoutSlash() throws MalformedURLException {
        System.out.println("testGetUrlDomainAbsoluteShortUrlWithoutSlash");
        String url = "http://www.google.co.uk";
        URL instance = new URL(url);
        String expResult = "http://www.google.co.uk/";
        String result = instance.getUrlDomain(url);
        assertEquals(expResult, result);
    }
    public void testGetUrlDomainAbsoluteShirtUrlWithoutScheme() throws MalformedURLException {
        System.out.println("testGetUrlDomainAbsoluteShirtUrlWithoutScheme");
        String url = "www.google.co.uk";
        URL instance = new URL(url);
        String expResult = "http://www.google.co.uk/";
        String result = instance.getUrlDomain(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of normaliseLink method, of class URL.
     * @throws java.net.MalformedURLException
     */
    public void testNormaliseRelativeParentFolderLink() throws MalformedURLException {
        System.out.println("testNormaliseRelativeLink");
        String baseUrl = "http://www.google.co.uk/path/file.php";
        String url = "../newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://www.google.co.uk/newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseRelativeSameFolderLink() throws MalformedURLException {
        System.out.println("testNormaliseRelativeSameFolderLink");
        String baseUrl = "http://www.google.co.uk/path/file.php";
        String url = "./newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://www.google.co.uk/path/newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseRelativeSameFolderShortBaseUrlLink() throws MalformedURLException {
        System.out.println("testNormaliseRelativeSameFolderShortBaseUrlLink");
        String baseUrl = "http://www.google.co.uk/";
        String url = "./newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://www.google.co.uk/newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseNoBaseUrlLink() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLink");
        String url = "http://www.google.com/newfile.jsp";
        URL instance = new URL(url);
        String expResult = url;
        String result = instance.normaliseLink(url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLink() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLink");
        String baseUrl = "http://www.google.co.uk/";
        String url = "http://www.google.com/newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = url;
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLinkLeadingSlash() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLinkLeadingSlash");
        String baseUrl = "http://www.google.co.uk/";
        String url = "http://www.google.com/newdir/";
        URL instance = new URL(baseUrl, url);
        String expResult = url;
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLinkNoLeadingSlash() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLinkNoLeadingSlash");
        String baseUrl = "http://www.google.co.uk/";
        String url = "http://www.google.com/newdir";
        URL instance = new URL(baseUrl, url);
        String expResult = url;
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLink2() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLink2");
        String baseUrl = "www.google.co.uk/";
        String url = "http://www.google.com/newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = url;
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLink3() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLink3");
        String baseUrl = "http://www.google.co.uk/path";
        String url = "/newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://www.google.co.uk/newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLink4() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLink4");
        String baseUrl = "http://www.google.co.uk/path";
        String url = "//newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseInvalidBaseUrl() throws MalformedURLException {
        System.out.println("testNormaliseInvalidBaseUrl");
        String baseUrl = "{{invalid}}";
        String url = "./newfile.jsp";
        URL instance = new URL(baseUrl, url);
        String expResult = "./newfile.jsp";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseInvalidUrl() throws MalformedURLException {
        System.out.println("testNormaliseInvalidUrl");
        String baseUrl = "http://www.google.co.uk/";
        String url = "{{invalid}}";
        URL instance = new URL(baseUrl, url);
        String expResult = "{{invalid}}";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }
    public void testNormaliseAbsoluteUrlLinkWithHash() throws MalformedURLException {
        System.out.println("testNormaliseAbsoluteUrlLinkWithHash");
        String baseUrl = "http://www.google.co.uk/";
        String url = "http://www.google.com/newfile.jsp?param=123#deeplink";
        URL instance = new URL(baseUrl, url);
        String expResult = "http://www.google.com/newfile.jsp?param=123";
        String result = instance.normaliseLink(baseUrl, url);
        assertEquals(expResult, result);
    }

}
