package com.globant.automation.trainings.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

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

    public static boolean isSubClassOf(Field field, Class<?> pomClass) {
        return pomClass != null && pomClass.isAssignableFrom(field.getType());
    }

    public static void injectFieldsPageObject(final Field field, final Object testInstance) {
        field.setAccessible(true);
        try {
            field.set(testInstance, field.getType().newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public static List<Field> getFieldsFilteringBy(final Object object, final Predicate<? super Field> filter) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null!");
        Field[] fields = object.getClass().getDeclaredFields();
        return stream(fields).filter(filter).collect(toList());
    }

    public static List<Field> getFieldsAnnotatedWith(final Object object, final Class<? extends Annotation> annotationClass) {
        return getFieldsFilteringBy(object, f -> f.isAnnotationPresent(annotationClass));
    }

    public static List<Field> getFieldsOfType(final Object object, final Class<?> fieldType) {
        return getFieldsFilteringBy(object, f -> f.getType().isAssignableFrom(fieldType));
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

    public static List<?> getFieldValuesAnnotatedWith(final Object object, final Class<? extends Annotation> annotationClass) {
        return getFieldsAnnotatedWith(object, annotationClass).stream().map(f -> {
            try {
                f.setAccessible(true);
                return f.get(object);
            } catch (IllegalAccessException e) {
                LOG.error(format("Could not retrieve %s annotated with %s!", f.getName(), annotationClass), e);
                return null;
            }
        }).collect(toList());
    }

}
