package com.dajudge.buql.analyzer.predicate;

import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;
import com.dajudge.buql.reflector.predicate.TypePredicate;

public class ComplexQueryTypePredicate implements ReflectorPredicate {
    private final Class<?> queryClass;

    private ComplexQueryTypePredicate(final Class<?> queryClass) {
        this.queryClass = queryClass;
    }

    public static ReflectorPredicate create(final Class<?> queryClass) {
        return new ComplexQueryTypePredicate(queryClass);
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return new TypePredicate(queryClass, o -> o).visit(visitor);
    }
}
