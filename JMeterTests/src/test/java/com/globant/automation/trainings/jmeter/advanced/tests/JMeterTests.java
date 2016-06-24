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

public class JMeterTests {

    private static final String jmeter = JMeterTests.class.getResource("/jmeter.properties").getFile();
    private static final Path jmeterPath = getDefault().getPath(jmeter.substring(1));

    @Before
    public void setUp() throws IOException {
        setJMeterHome(jmeterPath.getParent().toString());
        loadJMeterProperties(jmeterPath.toFile().getPath());
        loadProperties();
        initLogging(); // Comment this line out for extra logging i.e. DEBUG level
        initLocale();
    }

    @Test
    public void jmeterInsides() throws IOException {
        //JMeter Engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // JMeter Test Plan, basically JOrphan HashTree
        HashTree testPlanTree = new HashTree();

        // First HTTP Sampler - open example.com
        HTTPSampler examplecomSampler = new HTTPSampler();
        examplecomSampler.setDomain("google.com");
        examplecomSampler.setPort(80);
        examplecomSampler.setPath("/");
        examplecomSampler.setMethod("GET");
        examplecomSampler.setName("Open google.com");

        /**
         * If you are not planning on loading the generated JMX files from JMeter, you don't need
         * to set properties for JMeter's GUI elements.
         */
        examplecomSampler.setProperty(TEST_CLASS, HTTPSampler.class.getName());
        examplecomSampler.setProperty(GUI_CLASS, HttpTestSampleGui.class.getName());

        // Just because being in this network sucks...
        examplecomSampler.setProperty("HTTPSampler.proxyHost", "proxy.corp.globant.com");
        examplecomSampler.setProperty("HTTPSampler.proxyPort", "3128");

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
        TestPlan testPlan = new TestPlan("Create JMeter Script From Java Code");

        // Again, not *really* needed...
        testPlan.setProperty(TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

        // Construct Test Plan from previously initialized elements
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(examplecomSampler);

        // Optional: save generated test plan to JMeter's .jmx file format.
        // Just for completion's sake...
        SaveService.saveTree(testPlanTree, new FileOutputStream("example.jmx"));

        // Add Summarizer output to get test progress in stdout
        Summariser summer = null;
        String summariserName = getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        // Store execution results into a .jtl file
        String logFile = "example.jtl";
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(logFile);
        testPlanTree.add(testPlanTree.getArray()[0], logger);

        // Run Test Plan
        jmeter.configure(testPlanTree);
        jmeter.run();
    }
}

