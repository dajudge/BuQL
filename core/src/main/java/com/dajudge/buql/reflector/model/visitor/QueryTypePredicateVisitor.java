package com.dajudge.buql.reflector.model.visitor;

import com.dajudge.buql.reflector.annotations.BooleanOperationType;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.function.Function;

public interface QueryTypePredicateVisitor<T> {
    T booleanTypeOperator(BooleanOperationType booleanOperator, OperandAccessor booleanOperatorWrapper);

    T compare(String fieldName, Function<Object, Object> read, ReflectorCompareOperator operator);
}
