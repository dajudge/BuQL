package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.annotations.Column;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Function;

public abstract class FieldPredicate implements OperandWrapper {
    private final PropertyDescriptor prop;
    private final Function<Object, Object> parentAccessor;

    protected FieldPredicate(
            final PropertyDescriptor prop,
            final Function<Object, Object> parentAccessor
    ) {
        this.prop = prop;
        this.parentAccessor = parentAccessor;
    }

    @Override
    public <T> T visit(final QueryTypePredicateVisitor<T> visitor) {
        final String name = Optional.ofNullable(prop.getReadMethod().getAnnotation(Column.class))
                .map(Column::value)
                .orElse(prop.getName());
        return visit(visitor, name, this::read);
    }

    protected abstract <T> T visit(
            final QueryTypePredicateVisitor<T> visitor,
            final String fieldName,
            final Function<Object, Object> read
    );

    private Object read(Object o) {
        try {
            return prop.getReadMethod().invoke(parentAccessor.apply(o));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to read filter value from " + prop, e);
        }
    }
}
