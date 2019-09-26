package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.reflector.annotations.Transient;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExplicitTypeReflector  {
    private final BeanInfo bean;
    private final Function<Object, Object> parentAccessor;

    public ExplicitTypeReflector(
            final Class<?> type,
            final Function<Object, Object> parentAccessor
    ) {
        this.parentAccessor = parentAccessor;
        try {
            this.bean = Introspector.getBeanInfo(type);
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not introspect bean " + type.getName(), e);
        }
    }

    public List<ReflectorPredicate> getOperands() {
        return Stream.of(bean.getPropertyDescriptors())
                .filter(it -> it.getReadMethod() != null)
                .filter(it -> it.getReadMethod().getDeclaringClass() != Object.class)
                .filter(it -> it.getReadMethod().getAnnotation(Transient.class) == null)
                .map(p -> new TypePredicate(p.getPropertyType(), o -> read(o, p)))
                .collect(Collectors.toList());
    }

    private Object read(final Object o, final PropertyDescriptor prop) {
        try {
            return prop.getReadMethod().invoke(parentAccessor.apply(o));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to read filter value from " + prop, e);
        }
    }
}
