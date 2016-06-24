package com.globant.automation.trainings.jmeter.advanced.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;

import java.beans.PropertyDescriptor;

/**
 * Created by jkrzemien on 13/06/2016.
 */
public class CustomSamplerBeanInfo extends BeanInfoSupport {

    private static final String FILENAME = "filename";
    private static final String VARIABLE_NAME = "variableName";

    public CustomSamplerBeanInfo() {
        super(CustomSampler.class);

        createPropertyGroup("Ruso's Group of Properties", new String[]{
                FILENAME, VARIABLE_NAME
        });

        PropertyDescriptor p = property(FILENAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(VARIABLE_NAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    }

}