package com.dajudge.buql.reflector.model.visitor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class QueryFieldsWrapper implements OperandAccessor {
    private final BeanInfo bean;
    private final Function<Object, Object> parentAccessor;

    public QueryFieldsWrapper(
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

    @Override
    public List<OperandWrapper> getOperands() {
        return Stream.of(bean.getPropertyDescriptors())
                .filter(it -> it.getReadMethod() != null)
                .filter(it -> it.getReadMethod().getDeclaringClass() != Object.class)
                .map(prop -> new EqualsPredicate(prop, parentAccessor))
                .collect(toList());
    }
}
