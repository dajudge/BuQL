package com.dajudge.buql.reflector.model.visitor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class EqualsPredicate implements OperandWrapper {
    private final PropertyDescriptor prop;
    private final Function<Object, Object> parentAccessor;

    public EqualsPredicate(
            final PropertyDescriptor prop,
            final Function<Object, Object> parentAccessor
    ) {
        this.prop = prop;
        this.parentAccessor = parentAccessor;
    }

    @Override
    public <T> T visit(final QueryTypePredicateVisitor<T> visitor) {
        return visitor.equals(prop.getName(), this::read);
    }

    private Object read(Object o) {
        try {
            return prop.getReadMethod().invoke(parentAccessor.apply(o));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to read filter value from " + prop, e);
        }
    }
}
