package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.annotations.Like;
import com.dajudge.buql.reflector.annotations.Transient;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
                .filter(it -> it.getReadMethod().getAnnotation(Transient.class) == null)
                .map(this::createPredicate)
                .collect(toList());
    }

    private OperandWrapper createPredicate(final PropertyDescriptor prop) {
        if (prop.getReadMethod().getAnnotation(Like.class) != null) {
            return new LikePredicate(prop, parentAccessor);
        }
        return new EqualsPredicate(prop, parentAccessor);
    }
}
