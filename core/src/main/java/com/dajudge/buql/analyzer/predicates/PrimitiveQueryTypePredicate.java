package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.predicate.*;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.EQUALS;
import static java.util.function.Function.identity;

public class PrimitiveQueryTypePredicate implements ReflectorPredicate {
    private final String fieldName;

    public PrimitiveQueryTypePredicate(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.compare(new DatabaseFieldExpression(fieldName), new ParameterExpression(identity()), EQUALS);
    }
}
