package com.balnave.rambler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

public class RamblerReport {

    private static Comparator sortByParent = new Comparator<RamblerResult>() {

        @Override
        public int compare(RamblerResult o1, RamblerResult o2) {
            return o1.getParentUrl().compareTo(o1.getParentUrl());
        }
    };
    
    public static Collection<RamblerResult> getFailureResults(Collection<RamblerResult> results) {
        Collection<RamblerResult> failures = new ArrayList<RamblerResult>();
        for (RamblerResult result : results) {
            if (result.isFailureResult()) {
                failures.add(result);
            }
        }
        return failures;
    }
    
    public static Collection<RamblerResult> getErrorResults(Collection<RamblerResult> results) {
        Collection<RamblerResult> errors = new ArrayList<RamblerResult>();
        for (RamblerResult result : results) {
            if (result.isErrorResult()) {
                errors.add(result);
            }
        }
        return errors;
    }

    public static int getFailureCount(Collection<RamblerResult> results) {
        return getFailureResults(results).size();
    }
    
    public static int getErrorCount(Collection<RamblerResult> results) {
        return getErrorResults(results).size();
    }

    /**
     *
     * @param siteUrl
     * @param results
     * @param fileOut
     * @return
     */
    public static boolean toXMLFile(String siteUrl, Collection<RamblerResult> results, String fileOut) {
        return toXMLFile(siteUrl, results, new File(fileOut));
    }

    public static boolean toXMLFile(String siteUrl, Collection<RamblerResult> results, File fileOut) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Collections.sort((List<RamblerResult>) results, sortByParent);
            int failureCount = getFailureCount(results);
            int errorCount = getErrorCount(results);
            // root elements
            Document doc = docBuilder.newDocument();
            // testsuites element
            Element testsuites = doc.createElement("testsuites");
            testsuites.setAttribute("name", siteUrl);
            testsuites.setAttribute("tests", String.valueOf(results.size()));
            testsuites.setAttribute("failures", String.valueOf(failureCount));
            testsuites.setAttribute("errors", String.valueOf(errorCount));
            doc.appendChild(testsuites);
            // testsuite element
            Element testsuite = doc.createElement("testsuite");
            testsuite.setAttribute("id", "1");
            testsuite.setAttribute("name", String.format("Link Check for %s", siteUrl));
            testsuite.setAttribute("tests", String.valueOf(results.size()));
            testsuite.setAttribute("failures", String.valueOf(failureCount));
            testsuite.setAttribute("errors", String.valueOf(errorCount));
            testsuites.appendChild(testsuite);
            // testcase elements
            for (RamblerResult singleResult : results) {
                // staff elements
                Element testcase = doc.createElement("result");
                testcase.setAttribute("name", singleResult.getRequestUrl());
                testcase.setAttribute("parent", singleResult.getParentUrl());
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
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fileOut);
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            return false;
        } catch (TransformerException tfe) {
            return false;
        }
        return true;
    }
}
