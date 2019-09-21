package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

public class ComparisonPredicate extends FieldPredicate {
    private final ReflectorCompareOperator operator;
    private final Function<Object, Object> transform;

    public ComparisonPredicate(
            final PropertyDescriptor prop,
            final Function<Object, Object> parentAccessor,
            final ReflectorCompareOperator operator,
            final Function<Object, Object> transform
    ) {
        super(prop, parentAccessor);
        this.operator = operator;
        this.transform = transform;
    }

    @Override
    protected <T> T visit(
            final QueryTypePredicateVisitor<T> visitor,
            final String colName,
            final Function<Object, Object> read
    ) {
        return visitor.compare(colName, read, operator, transform);
    }
}
