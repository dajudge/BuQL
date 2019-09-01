package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.annotations.BooleanOperator;

import java.util.function.Function;

import static com.dajudge.buql.reflector.annotations.BooleanOperationType.AND;

public class QueryTypeWrapper implements OperandWrapper {
    private final Class<?> type;
    private final Function<Object, Object> parentAccessor;

    public QueryTypeWrapper(final Class<?> type, final Function<Object, Object> parentAccessor) {
        this.type = type;
        this.parentAccessor = parentAccessor;
    }

    @Override
    public <T> T visit(final QueryTypePredicateVisitor<T> queryTypePredicateVisitor) {
        final BooleanOperator booleanOperator = type.getAnnotation(BooleanOperator.class);
        if (booleanOperator != null) {
            return queryTypePredicateVisitor.booleanTypeOperator(
                    booleanOperator.value(),
                    new BooleanOperatorWrapper(type, parentAccessor)
            );
        }
        return queryTypePredicateVisitor.booleanTypeOperator(AND, new QueryFieldsWrapper(type, parentAccessor));
    }
}
