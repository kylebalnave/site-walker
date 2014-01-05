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
 * Exports a summary report to an XML file
 * @author balnave
 */
public class Summary extends AbstractReport {

    public Summary(Config config, List<Result> results) {
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
            Element summary = doc.createElement("summary");
            summary.setAttribute("url", config.getSiteUrl());
            summary.setAttribute("results", String.valueOf(results.size()));
            summary.setAttribute("failures", String.valueOf(failureCount));
            summary.setAttribute("errors", String.valueOf(errorCount));
            doc.appendChild(summary);
            // testcase elements
            for (Result singleResult : results) {
                // staff elements
                String status = "pass";
                if (singleResult.isErrorResult()) {
                    status = "error";
                } else if (singleResult.isFailureResult()) {
                    status = "failed";
                }
                Element result = doc.createElement("result");
                result.setAttribute("url", singleResult.getRequestUrl());
                result.setAttribute("code", String.valueOf(singleResult.getResponseStatus()));
                result.setAttribute("status", status);
                result.setAttribute("message", singleResult.getResponseMessage());
                // source
                Element source = doc.createElement("source");
                source.setAttribute("type", singleResult.getContentType());
                source.setTextContent(singleResult.getReponseSource());
                result.appendChild(source);
                // child links
                Element childlinks = doc.createElement("childlinks");
                source.setAttribute("count", String.valueOf(singleResult.getChildLinks().size()));
                for (String href : singleResult.getChildLinks()) {
                    Element link = doc.createElement("link");
                    link.setAttribute("url", href);
                    childlinks.appendChild(link);
                }
                // parent links
                Element parentlinks = doc.createElement("parentlinks");
                source.setAttribute("count", String.valueOf(singleResult.getParentUrls().size()));
                for (String href : singleResult.getParentUrls()) {
                    Element link = doc.createElement("link");
                    link.setAttribute("url", href);
                    parentlinks.appendChild(link);
                }
                // append to the summary
                summary.appendChild(result);
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
