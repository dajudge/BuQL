package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.annotations.BooleanOperationType;

import java.util.function.Function;

public interface QueryTypePredicateVisitor<T> {
    T booleanTypeOperator(BooleanOperationType booleanOperator, OperandAccessor booleanOperatorWrapper);

    T equals(String fieldName, Function<Object, Object> read);
}
