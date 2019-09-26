package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ParameterExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.EQUALS;
import static java.util.function.Function.identity;

public class PrimitiveQueryTypePredicate implements ReflectorPredicate {
    private final String colName;

    public PrimitiveQueryTypePredicate(final String colName) {
        this.colName = colName;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.compare(
                new DatabaseFieldExpression(colName),
                new ParameterExpression(identity()),
                EQUALS
        );
    }
}
