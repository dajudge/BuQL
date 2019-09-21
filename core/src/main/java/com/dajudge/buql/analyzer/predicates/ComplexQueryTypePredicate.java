package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.model.translate.ComplexQueryTypePredicateVisitor;
import com.dajudge.buql.reflector.model.visitor.QueryTypeWrapper;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

public class ComplexQueryTypePredicate implements ReflectorPredicate {
    private final ReflectorPredicate predicate;

    public ComplexQueryTypePredicate(final Class<?> queryClass) {
        predicate = new QueryTypeWrapper(queryClass, o -> o).visit(new ComplexQueryTypePredicateVisitor());
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return predicate.visit(visitor);
    }
}
