package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import com.balnave.rambler.logging.Logger;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles XML reporting
 *
 * @author kyleb2
 */
public class XmlReport extends AbstractReport {

    protected final Document doc;

    public XmlReport(Config config, List<Result> results) {
        super(config, results);
        doc = getDoc();
    }

    protected final Document getDoc() {
        Document _doc = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            _doc = docBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            Logger.log(String.format("Error creating XML doc : %s", ex.getMessage()), Logger.WARNING);
        }
        return _doc;
    }

    @Override
    public void out() {
        new SystemLog(config, results).out();
    }

    @Override
    public boolean out(String fileOut) {
        buildResultDoc();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.log("Failed to create XML transformer %s", Logger.WARNING);
            return false;
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fileOut);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.log("Failed to transform XML %s", Logger.WARNING);
            return false;
        }
        return true;
    }

    protected Document buildResultDoc() {
        Element site = doForEachResultList(results);
        for (Result result : results) {
            if (result.isErrorResult()) {
                site.appendChild(doForEachErrorResult(result));
            } else if (result.isFailureResult()) {
                site.appendChild(doForEachFailedResult(result));
            } else {
                site.appendChild(doForEachPassedResult(result));
            }
        }
        return doc;
    }

    @Override
    protected Element doForEachResultList(List<Result> results) {
        Element element = doc.createElement("rambler");
        element.setAttribute("url", config.getSiteUrl());
        element.setAttribute("tests", String.valueOf(this.getResultsCount()));
        element.setAttribute("failures", String.valueOf(this.getFailureCount()));
        element.setAttribute("errors", String.valueOf(this.getErrorCount()));
        doc.appendChild(element);
        return element;
    }

    @Override
    protected Element doForEachPassedResult(Result result) {
        Element element = doc.createElement("result");
        element.setAttribute("url", result.getRequestUrl());
        element.setAttribute("code", String.valueOf(result.getResponseStatus()));
        element.setAttribute("status", "pass");
        // source
        if (config.isRetainHtmlSource()) {
            Element source = doc.createElement("source");
            source.setAttribute("type", result.getContentType());
            source.setTextContent(result.getReponseSource());
            element.appendChild(source);
        }
        // child links
        if (config.isRetainChildLinks()) {
            Element childlinks = doc.createElement("childurls");
            childlinks.setAttribute("count", String.valueOf(result.getChildLinks().size()));
            for (String href : result.getChildLinks()) {
                Element link = doc.createElement("link");
                link.setAttribute("url", href);
                childlinks.appendChild(link);
            }
            element.appendChild(childlinks);
        }
        // parent links
        if (config.isRetainParentLinks()) {
            Element parentlinks = doc.createElement("parenturls");
            parentlinks.setAttribute("count", String.valueOf(result.getParentUrls().size()));
            for (String href : result.getParentUrls()) {
                Element link = doc.createElement("link");
                link.setAttribute("url", href);
                parentlinks.appendChild(link);
            }
            element.appendChild(parentlinks);
        }
        return element;
    }

    @Override
    protected Element doForEachFailedResult(Result result) {
        Element element = doForEachPassedResult(result);
        Element msg = doc.createElement("message");
        msg.setTextContent(result.getResponseMessage());
        element.setAttribute("status", "fail");
        element.appendChild(msg);
        return element;
    }

    @Override
    protected Element doForEachErrorResult(Result result) {
        Element element = doForEachFailedResult(result);
        element.setAttribute("status", "fail");
        return element;
    }

}
