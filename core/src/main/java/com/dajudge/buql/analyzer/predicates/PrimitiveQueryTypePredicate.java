package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ParameterExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import static java.util.function.Function.identity;

public class PrimitiveQueryTypePredicate implements ReflectorPredicate {
    private final String fieldName;

    public PrimitiveQueryTypePredicate(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.eq(new DatabaseFieldExpression(fieldName), new ParameterExpression(identity()));
    }
}
