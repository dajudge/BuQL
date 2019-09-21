package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

public class ComparisonPredicate extends FieldPredicate {
    private final ReflectorCompareOperator operator;

    public ComparisonPredicate(
            final PropertyDescriptor prop,
            final Function<Object, Object> parentAccessor,
            final ReflectorCompareOperator operator
    ) {
        super(prop, parentAccessor);
        this.operator = operator;
    }

    @Override
    protected <T> T visit(
            final QueryTypePredicateVisitor<T> visitor,
            final String fieldName,
            final Function<Object, Object> read
    ) {
        return visitor.compare(fieldName, read, operator);
    }
}
