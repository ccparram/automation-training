package com.globant.automation.training.arquillian;
/**
 * @author Juan Krzemien
 */
public class TemperatureConverter {

    public double convertToCelsius(double f) {
        return ((f - 32) * 5 / 9);
    }

    public double convertToFarenheit(double c) {
        return ((c * 9 / 5) + 32);
    }

}
