package com.balnave.rambler.reports;

import com.balnave.rambler.Config;
import com.balnave.rambler.Result;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Exports a Junit TestSuite to an XML file
 *
 * @author balnave
 */
public class Junit extends XmlReport {

    public Junit(Config config, List<Result> results) {
        super(config, results);
    }

    @Override
    protected Element doForEachResultList(List<Result> results) {
        // testsuites element
        Element testsuites = doc.createElement("testsuites");
        testsuites.setAttribute("name", config.getSiteUrl());
        testsuites.setAttribute("tests", String.valueOf(this.getResultsCount()));
        testsuites.setAttribute("failures", String.valueOf(this.getFailureCount()));
        testsuites.setAttribute("errors", String.valueOf(this.getErrorCount()));
        testsuites.setAttribute("time", String.valueOf(this.getResultsTimeMs()));
        testsuites.setAttribute("timestamp", String.valueOf(this.getTimeStamp()));
        doc.appendChild(testsuites);
        // testsuite element
        Element testsuite = doc.createElement("testsuite");
        testsuite.setAttribute("id", "1");
        testsuite.setAttribute("name", String.format("Rambler URL: %s", config.getSiteUrl()));
        testsuites.setAttribute("tests", String.valueOf(this.getResultsCount()));
        testsuites.setAttribute("failures", String.valueOf(this.getFailureCount()));
        testsuites.setAttribute("errors", String.valueOf(this.getErrorCount()));
        testsuites.setAttribute("time", String.valueOf(this.getResultsTimeMs()));
        testsuites.appendChild(testsuite);
        return testsuite;
    }

    @Override
    protected Element doForEachPassedResult(Result result) {
        // staff elements
        Element element = doc.createElement("testcase");
        element.setAttribute("name", result.getRequestUrl());
        element.setAttribute("tests", "1");
        element.setAttribute("assertions", "1");
        element.setAttribute("failures", result.isFailureResult() ? "1" : "0");
        element.setAttribute("errors", result.isErrorResult() ? "1" : "0");
        element.setAttribute("time", String.valueOf(result.getLoadDurationMs()));
        return element;
    }

    @Override
    protected Element doForEachFailedResult(Result result) {
        Element element = doForEachPassedResult(result);
        Element failure = doc.createElement("failure");
        failure.setAttribute("type", String.valueOf(result.getResponseStatus()) + " Failure");
        failure.setAttribute("message", String.valueOf(result.getResponseStatus()) + ": " + result.getResponseMessage());
        element.appendChild(failure);
        return element;
    }

    @Override
    protected Element doForEachErrorResult(Result result) {
        Element element = doForEachPassedResult(result);
        Element error = doc.createElement("error");
        error.setAttribute("type", String.valueOf(result.getResponseStatus()) + " Error");
        error.setAttribute("message", String.valueOf(result.getResponseStatus()) + ": " + result.getResponseMessage());
        element.appendChild(error);
        return element;
    }
}
