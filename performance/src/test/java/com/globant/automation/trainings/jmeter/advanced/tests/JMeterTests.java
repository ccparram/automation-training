package com.globant.automation.trainings.jmeter.advanced.tests;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.FileSystems.getDefault;
import static org.apache.jmeter.save.SaveService.loadProperties;
import static org.apache.jmeter.testelement.TestElement.GUI_CLASS;
import static org.apache.jmeter.testelement.TestElement.TEST_CLASS;
import static org.apache.jmeter.util.JMeterUtils.*;

/**
 * Simple example showing how to model a JMeter's JMX file as a JUnit test.
 * <p>
 * Created by jkrzemien on 13/06/2016.
 */
public class JMeterTests {

    private static final String JMETER = JMeterTests.class.getResource("/jmeter.properties").getFile();
    private static final Path JMETER_PATH = getDefault().getPath(JMETER.substring(1));
    private static final String OUTPUT_JMX_FILE = "example.jmx";
    private static final String OUTPUT_JTL_FILE = "example.jtl";
    private static final String PROXY_HOST = "proxy.corp.globant.com";
    private static final String PROXY_PORT = "3128";
    private static final String TARGET = "google.com";

    @Before
    public void setUp() throws IOException {
        setJMeterHome(JMETER_PATH.getParent().toString());
        loadJMeterProperties(JMETER_PATH.toFile().getPath());
        loadProperties();
        initLogging(); // Comment this line out for extra logging i.e. DEBUG level
        initLocale();
    }

    @Test
    public void jmeterInsides() throws IOException {
        // A HTTP Sampler - opens TARGET
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setDomain(TARGET);
        httpSampler.setPort(80);
        httpSampler.setPath("/");
        httpSampler.setMethod("GET");
        httpSampler.setName("Open " + TARGET);

        /**
         * If you are not planning on loading the generated JMX files from JMeter, you don't need
         * to set properties for JMeter's GUI elements.
         */
        httpSampler.setProperty(TEST_CLASS, HTTPSampler.class.getName());
        httpSampler.setProperty(GUI_CLASS, HttpTestSampleGui.class.getName());

        // Just because being in this network sucks...
        httpSampler.setProperty(HTTPSampler.PROXYHOST, PROXY_HOST);
        httpSampler.setProperty(HTTPSampler.PROXYPORT, PROXY_PORT);

        // Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);

        // Again, not *really* needed...
        loopController.setProperty(TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(GUI_CLASS, LoopControlPanel.class.getName());

        loopController.initialize();

        // Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Example Thread Group");
        threadGroup.setNumThreads(10);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController(loopController);

        // Again, not *really* needed...
        threadGroup.setProperty(TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(GUI_CLASS, ThreadGroupGui.class.getName());

        // Test Plan
        TestPlan testPlan = new TestPlan("A JMeter Test Plan From Java Code");

        // Again, not *really* needed...
        testPlan.setProperty(TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

        // JMeter Test Plan, basically JOrphan HashTree
        HashTree testPlanTree = new HashTree();
        // Construct Test Plan from previously initialized elements
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(httpSampler);

        // Optional: save generated test plan to JMeter's .jmx file format.
        // Just for completion's sake...
        SaveService.saveTree(testPlanTree, new FileOutputStream(OUTPUT_JMX_FILE));

        // Add Summarizer output to get test progress in stdout
        Summariser summariser = new Summariser("Summary");

        // Store execution results into a .jtl file
        ResultCollector logger = new ResultCollector(summariser);
        logger.setFilename(OUTPUT_JTL_FILE);
        testPlanTree.add(testPlanTree.getArray()[0], logger);

        // Run Test Plan
        //JMeter Engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        jmeter.configure(testPlanTree);
        jmeter.run();
    }
}

