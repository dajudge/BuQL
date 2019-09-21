package com.dajudge.buql.reflector.model.visitor;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

public class LikePredicate extends FieldPredicate {

    public LikePredicate(final PropertyDescriptor prop, final Function<Object, Object> parentAccessor) {
        super(prop, parentAccessor);
    }

    @Override
    protected <T> T visit(
            final QueryTypePredicateVisitor<T> visitor,
            final String fieldName,
            final Function<Object, Object> read
    ) {
        return visitor.like(fieldName, read);
    }
}
