package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Exports a Checkstyle report to an XML file
 *
 * @author balnave
 */
public class Checkstyle extends AbstractReport {

    public Checkstyle(Config config, List<Result> results) {
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
            Element testsuites = doc.createElement("checkstyle");
            doc.appendChild(testsuites);
            // testcase elements
            for (Result singleResult : results) {
                if (singleResult.isFailureResult() || singleResult.isErrorResult()) {
                    Element testcase = doc.createElement("file");
                    testcase.setAttribute("name", singleResult.getRequestUrl());
                    String statusStr = String.valueOf(singleResult.getResponseStatus());
                    Element failure = doc.createElement("error");
                    failure.setAttribute("line", "0");
                    failure.setAttribute("column", "0");
                    failure.setAttribute("message", statusStr + ": " + singleResult.getResponseMessage());
                    failure.setAttribute("source", statusStr + ": " + singleResult.getRequestUrl());
                    failure.setAttribute("severity", "error");
                    testcase.appendChild(failure);
                    testsuites.appendChild(testcase);
                }
            }
            //
            // write the content into xml file
            if (fileOut != null) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
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
