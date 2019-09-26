package com.dajudge.buql.reflector.predicate;

public interface ReflectorPredicateVisitor<T> {
    T booleanOperation(BooleanOperationPredicate predicate);

    T compare(ReflectorExpression left, ReflectorExpression right, ReflectorCompareOperator operator);

    T isNull(ReflectorExpression expression);
}
