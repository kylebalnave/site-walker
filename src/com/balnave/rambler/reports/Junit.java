package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Exports a Junit TestSuite to an XML file
 * @author balnave
 */
public class Junit extends AbstractReport {

    public Junit(Config config, List<Result> results) {
        super(config, results);
    }

    @Override
    public boolean out(String fileOut) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Collections.sort(results, sortResponseStatus);
            int failureCount = getFailureCount();
            int errorCount = getErrorCount();
            // root elements
            Document doc = docBuilder.newDocument();
            // testsuites element
            Element testsuites = doc.createElement("testsuites");
            testsuites.setAttribute("name", config.getSiteUrl());
            testsuites.setAttribute("includes", config.getIncludesRegExp());
            testsuites.setAttribute("excludes", config.getExcludesRegExp());
            testsuites.setAttribute("tests", String.valueOf(results.size()));
            testsuites.setAttribute("failures", String.valueOf(failureCount));
            testsuites.setAttribute("errors", String.valueOf(errorCount));
            doc.appendChild(testsuites);
            // testsuite element
            Element testsuite = doc.createElement("testsuite");
            testsuite.setAttribute("id", "1");
            testsuite.setAttribute("name", String.format("Link Check for %s", config.getSiteUrl()));
            testsuite.setAttribute("tests", String.valueOf(results.size()));
            testsuite.setAttribute("failures", String.valueOf(failureCount));
            testsuite.setAttribute("errors", String.valueOf(errorCount));
            testsuites.appendChild(testsuite);
            // testcase elements
            for (Result singleResult : results) {
                // staff elements
                Element testcase = doc.createElement("result");
                testcase.setAttribute("name", singleResult.getRequestUrl());
                testcase.setAttribute("tests", "1");
                testcase.setAttribute("assertions", "1");
                testcase.setAttribute("failures", singleResult.isFailureResult() ? "1" : "0");
                testcase.setAttribute("errors", singleResult.isErrorResult() ? "1" : "0");
                String statusStr = String.valueOf(singleResult.getResponseStatus());
                if (singleResult.isFailureResult()) {
                    Element failure = doc.createElement("failure");
                    failure.setAttribute("type", statusStr + " Failure");
                    failure.setAttribute("message", statusStr + ": " + singleResult.getResponseMessage());
                    testcase.appendChild(failure);
                } else if (singleResult.isErrorResult()) {
                    Element failure = doc.createElement("error");
                    failure.setAttribute("type", statusStr + " Error");
                    failure.setAttribute("message", statusStr + ": " + singleResult.getResponseMessage());
                    testcase.appendChild(failure);
                }
                testsuite.appendChild(testcase);
            }
            //
            // write the content into xml file
            if (fileOut != null) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fileOut);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException pce) {
            return false;
        } catch (TransformerException tfe) {
            return false;
        }
        return true;
    }

    @Override
    public void out() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
