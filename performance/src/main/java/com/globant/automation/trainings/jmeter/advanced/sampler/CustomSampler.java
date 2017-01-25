package com.globant.automation.trainings.jmeter.advanced.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;

/**
 * Custom Sampler
 * <p>
 * JMeter creates an instance of a sampler class for every occurrence of the
 * element in every thread. [some additional copies may be created before the
 * test run starts]
 * <p>
 * Thus each sampler is guaranteed to be called by a single thread - there is no
 * need to synchronize access to instance variables.
 * <p>
 * However, access to class fields must be synchronized.
 * <p>
 * Created by jkrzemien on 13/06/2016.
 */
public class CustomSampler extends AbstractSampler implements TestBean {

    private String filename;

    private String variableName;

    /**
     * {@inheritDoc}
     */
    @Override
    public SampleResult sample(Entry e) {
        SampleResult res = new SampleResult();
        boolean isOK = false; // Did sample succeed?
        String response;

        res.setSampleLabel(getTitle());
        /*
         * Perform the sampling
         */
        res.sampleStart(); // Start timing
        try {

            // Do something here ...
            response = Thread.currentThread().getName() + " " + filename + " " + variableName;

            /*
             * Set up the sample result details
             */
            res.setSamplerData(getData());
            res.setResponseData(response, null);
            res.setDataType(SampleResult.TEXT);

            res.setResponseCodeOK();
            res.setResponseMessage("OK");
            isOK = true;
        } catch (Exception ex) {
            res.setResponseCode("500");
            res.setResponseMessage(ex.toString());
        }
        res.sampleEnd(); // End timing

        res.setSuccessful(isOK);

        return res;
    }

    /**
     * @return a string for the sampleResult Title
     */
    private String getTitle() {
        return this.getName();
    }

    /**
     * @return the test data for the sample
     */
    public String getData() {
        return getPropertyAsString("This is an example of sampler data");
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String fileName) {
        this.filename = fileName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
