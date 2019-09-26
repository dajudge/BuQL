package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.predicate.TypePredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

public class ComplexQueryTypePredicate implements ReflectorPredicate {
    private final Class<?> queryClass;

    public ComplexQueryTypePredicate(final Class<?> queryClass) {
        this.queryClass = queryClass;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return new TypePredicate(queryClass, o -> o).visit(visitor);
    }
}
