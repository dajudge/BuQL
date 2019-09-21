package com.dajudge.buql.reflector.model.visitor;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

public class EqualsPredicate extends FieldPredicate {
    public EqualsPredicate(final PropertyDescriptor prop, final Function<Object, Object> parentAccessor) {
        super(prop, parentAccessor);
    }

    @Override
    protected <T> T visit(
            final QueryTypePredicateVisitor<T> visitor,
            final String fieldName,
            final Function<Object, Object> read
    ) {
        return visitor.equals(fieldName, read);
    }
}
