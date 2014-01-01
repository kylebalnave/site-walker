/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.balnave.rambler.RamblerResult;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author balnave
 */
public class RamblerResultTest {
    
    public RamblerResultTest() {
    }
    
    protected static HttpURLConnection validConnection;
    protected static HttpURLConnection invalid404Connection;
    
    @BeforeClass
    public static void setUpClass() {
        try {
            validConnection = (HttpURLConnection)new URL("http://www.google.co.uk").openConnection();
        } catch (MalformedURLException ex) {
            Logger.getLogger(RamblerResultTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RamblerResultTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getResponseStatus method, of class RamblerResult.
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testGet200ResponseStatus() throws MalformedURLException {
        System.out.println("get200ResponseStatus");
        RamblerResult instance = new RamblerResult(validConnection);
        int expResult = 200;
        int result = instance.getResponseStatus();
        assertEquals(String.format("Expecting %s but got %s", expResult, result), expResult, result);
    }
    /*
    @Test
    public void testGet404ResponseStatus() throws MalformedURLException {
        System.out.println("get404ResponseStatus");
        RamblerResult instance = new RamblerResult(invalid404Connection);
        int expResult = 404;
        int result = instance.getResponseStatus();
        assertEquals(String.format("Expecting %s but got %s", expResult, result), expResult, result);
    }
    */

    /**
     * Test of getResponseMessage method, of class RamblerResult.
     */
    @Test
    public void testGetResponseMessage() {
        System.out.println("getResponseMessage");
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = "OK";
        String result = instance.getResponseMessage();
        assertEquals(String.format("Expecting %s but got %s", expResult, result), expResult, result);
    }

    /**
     * Test of getRequestUrl method, of class RamblerResult.
     */
    @Test
    public void testGetRequestUrl() {
        System.out.println("getRequestUrl");
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = "http://www.google.co.uk";
        String result = instance.getRequestUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLinks method, of class RamblerResult.
     */
    //@Test
    public void testGetLinks() {
        System.out.println("getLinks");
        RamblerResult instance = new RamblerResult(validConnection);
        instance.setReponseSource("<a href='http://www.google.co.uk'>hello world</a>");
        List<String> result = instance.getLinks();
        assertTrue(String.format("Expecting > 0 but got %s", result.size()), result.size() > 0);
    }

    /**
     * Test of searchSourceForLinks method, of class RamblerResult.
     */
    @Test
    public void testSearchSourceForLinks() {
        System.out.println("searchSourceForLinks");
        RamblerResult instance = new RamblerResult(validConnection);
        String source = "<a href='http://www.google.co.uk'>hello world</a>";
        List<String> result = instance.searchSourceForLinks(source);
        assertTrue(String.format("Expecting > 0 but got %s", result.size()), result.size() > 0);
    }

    /**
     * Test of normaliseLink method, of class RamblerResult.
     * @throws java.net.URISyntaxException
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testNormaliseAbsoluteLink1() throws URISyntaxException, MalformedURLException {
        System.out.println("normaliseAbsoluteLink1");
        String baseUrl = "http://www.google.com";
        String href = "http://anotherlink.com/link.php";
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = href;
        String result = instance.normaliseLink(baseUrl, href);
        assertEquals(expResult, result);
    }
    @Test
    public void testNormaliseAbsoluteLink2() throws URISyntaxException, MalformedURLException {
        System.out.println("normaliseAbsoluteLink2");
        String baseUrl = "http://www.google.com/";
        String href = "www.anotherlink.com/link.php";
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = href;
        String result = instance.normaliseLink(baseUrl, href);
        assertEquals(expResult, result);
    }
    @Test
    public void testNormaliseAbsoluteLink3() throws URISyntaxException, MalformedURLException {
        System.out.println("normaliseAbsoluteLink3");
        String baseUrl = "http://www.google.com/";
        String href = "/link.php";
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = "http://www.google.com/link.php";
        String result = instance.normaliseLink(baseUrl, href);
        assertEquals(expResult, result);
    }
    @Test
    public void testNormaliseRelativeLink1() throws URISyntaxException, MalformedURLException {
        System.out.println("normaliseRelativeLink1");
        String baseUrl = "http://www.google.com/";
        String href = "./link.php";
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = "http://www.google.com/link.php";
        String result = instance.normaliseLink(baseUrl, href);
        assertEquals(expResult, result);
    }
    @Test
    public void testNormaliseRelativeLink2() throws URISyntaxException, MalformedURLException {
        System.out.println("normaliseRelativeLink2");
        String baseUrl = "http://www.google.com/testdir/";
        String href = "../link.php";
        RamblerResult instance = new RamblerResult(validConnection);
        String expResult = "http://www.google.com/link.php";
        String result = instance.normaliseLink(baseUrl, href);
        assertEquals(expResult, result);
    }
    
}
