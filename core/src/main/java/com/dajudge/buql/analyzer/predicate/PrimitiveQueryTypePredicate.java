package com.dajudge.buql.analyzer.predicates;

import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ParameterExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.EQUALS;
import static java.util.function.Function.identity;

public class PrimitiveQueryTypePredicate implements ReflectorPredicate {
    private final String colName;

    private PrimitiveQueryTypePredicate(final String colName) {
        this.colName = colName;
    }

    public static ReflectorPredicate create(final Class<?> actualQueryClass, final String predicateName) {
        return new PrimitiveQueryTypePredicate(predicateName);
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
