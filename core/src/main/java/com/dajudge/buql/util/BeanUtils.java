package com.dajudge.buql.util;

import com.dajudge.buql.reflector.annotations.Transient;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.stream.Stream;

public final class BeanUtils {
    private BeanUtils() {
    }

    public static BeanInfo getBeanInfo(final Class<?> type) {
        try {
            return Introspector.getBeanInfo(type);
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not introspect bean " + type.getName(), e);
        }
    }

    public static Stream<PropertyDescriptor> getRelevantProperties(final BeanInfo bean) {
        return Stream.of(bean.getPropertyDescriptors())
                .filter(it -> it.getReadMethod() != null)
                .filter(it -> it.getReadMethod().getDeclaringClass() != Object.class)
                .filter(it -> it.getReadMethod().getAnnotation(Transient.class) == null);
    }
}
