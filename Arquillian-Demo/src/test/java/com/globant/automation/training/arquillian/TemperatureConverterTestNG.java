package com.globant.automation.training.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author Juan Krzemien
 */
public class TemperatureConverterTestNG { //extends Arquillian {

    @Inject
    private TemperatureConverter converter;

    @Deployment
    public static JavaArchive createTestArchive() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClasses(TemperatureConverter.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Test
    public void testConvertToCelsius() {
        assertEquals(converter.convertToCelsius(32d), 0d, 0.001d);
        assertEquals(converter.convertToCelsius(212d), 100d, 0.001d);
    }


    @Test
    public void testConvertToFarenheit() {
        assertEquals(converter.convertToFarenheit(0d), 32d, 0.001d);
        assertEquals(converter.convertToFarenheit(100d), 212d, 0.001d);
    }

}