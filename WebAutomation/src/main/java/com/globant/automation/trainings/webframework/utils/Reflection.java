package com.globant.automation.trainings.webframework.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * Helper utility class to deal with typical reflective tasks
 *
 * @author Juan Krzemien
 */
public final class Reflection {

    private static final Logger LOG = LoggerFactory.getLogger(Reflection.class);

    private Reflection() {
    }

    public static List<Field> getFieldsOfType(final Object object, final Class<?> fieldType) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null!");
        Field[] fields = object.getClass().getDeclaredFields();
        return stream(fields).filter(f -> f.getType().isAssignableFrom(fieldType)).collect(toList());
    }

    public static <T> List<T> getFieldValuesOfType(final Object object, final Class<?> fieldType) {
        return getFieldsOfType(object, fieldType).stream().map(f -> {
            try {
                f.setAccessible(true);
                return (T) f.get(object);
            } catch (IllegalAccessException e) {
                LOG.error(format("Could not retrieve %s from %s!", fieldType, object), e);
                return null;
            }
        }).collect(toList());
    }

}
