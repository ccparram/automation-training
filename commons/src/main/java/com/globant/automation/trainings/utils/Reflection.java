package com.globant.automation.trainings.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static boolean fieldIsSubClassOf(Field field, Class<?> aClass) {
        return aClass != null && aClass.isAssignableFrom(field.getType());
    }

    public static Object createOwnDefaultInstance(Field field) {
        try {
            return field.getType().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error(format("Could not create default instance for %s", field.getType()), e);
        }
        return null;
    }

    public static void setField(Field field, Object target, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            LOG.error(format("Could not set field %s in %s using %s", field.getName(), target.toString(), value.toString()), e);
        }
    }

    public static Object injectFieldOwnInstance(Field field, Object target) {
        Object instance = createOwnDefaultInstance(field);
        setField(field, target, instance);
        return instance;
    }

    public static List<Field> getFieldsFilteringBy(Object object, Predicate<? super Field> filter) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null!");
        Field[] fields = object.getClass().getDeclaredFields();
        return stream(fields).filter(filter).collect(toList());
    }

    public static List<Field> getFieldsAnnotatedWith(Object object, Class<? extends Annotation> annotationClass) {
        return getFieldsFilteringBy(object, f -> f.isAnnotationPresent(annotationClass));
    }

    public static List<Field> getFieldsOfType(Object object, Class<?> fieldType) {
        return getFieldsFilteringBy(object, f -> f.getType().isAssignableFrom(fieldType));
    }

    public static <T> List<T> getFieldValuesOfType(Object object, Class<?> fieldType) {
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

    public static List<?> getFieldValuesAnnotatedWith(Object object, Class<? extends Annotation> annotationClass) {
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

    public static Class<?> getBaseClass(Field field) {
        Class<?> baseClass = field.getType();
        while (baseClass.getSuperclass() != Object.class) {
            baseClass = baseClass.getSuperclass();
        }
        return baseClass;
    }

}
