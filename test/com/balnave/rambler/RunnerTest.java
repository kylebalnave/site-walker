/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.balnave.rambler;

import com.balnave.rambler.queue.QueueItem;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import junit.framework.TestCase;

class SimpleServer {

    public static HttpServer server;

    public static void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 7777), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static void stop() {
        server.stop(1);
        server = null;
    }

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello World!";
            byte[] bytes = response.getBytes();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }

    }
}

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
        SimpleServer.start();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        SimpleServer.stop();
    }

    /**
     * Test of call method, of class Runner.
     */
    public void testCall200() throws Exception {
        System.out.println("testCall200");
        Runner instance = new Runner(new QueueItem("http://127.0.0.1:7777"));
        int expResult = 200;
        Result result = instance.call();
        assertEquals(expResult, result.getResponseStatus());
    }

    /**
     * Test of getResult method, of class Runner.
     */
    public void testGetResult() throws Exception {
        System.out.println("getResult");
        Runner instance = new Runner(new QueueItem("http://127.0.0.1:7777/"));
        Result result = instance.call();
        Result expResult = instance.getResult();
        assertEquals(expResult, result);
    }

}
