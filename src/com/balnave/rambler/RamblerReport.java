package com.balnave.rambler;


import java.io.File;
import java.util.Collection;
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
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("site");
            rootElement.setAttribute("url", siteUrl);
            rootElement.setAttribute("tests", "0");
            rootElement.setAttribute("failures", "0");
            doc.appendChild(rootElement);
            //
            // create the child nodes
            for (RamblerResult singleResult : results) {
                // staff elements
                Element resultElement = doc.createElement("result");
                resultElement.setAttribute("url", singleResult.getRequestUrl());
                resultElement.setAttribute("tests", "1");
                resultElement.setAttribute("status", String.valueOf(singleResult.getResponseStatus()));
                resultElement.setAttribute("message", singleResult.getResponseMessage());
                rootElement.appendChild(resultElement);
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
