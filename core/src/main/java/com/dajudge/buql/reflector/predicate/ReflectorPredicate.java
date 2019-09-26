package com.dajudge.buql.reflector.predicate;

public interface ReflectorPredicate {
    <T> T visit(ReflectorPredicateVisitor<T> visitor);
}
