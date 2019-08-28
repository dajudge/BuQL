package com.dajudge.buql.reflector.predicate;

public interface ReflectorPredicateVisitor<T> {
    T or(BooleanOperationPredicate predicate);

    T and(BooleanOperationPredicate predicate);

    T eq(ReflectorExpression left, ReflectorExpression right);
}
