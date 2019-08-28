package com.dajudge.buql.query.model.expression;

import java.util.List;

import static java.util.Arrays.asList;

public interface QueryPredicate extends QueryExpression {
    static QueryPredicate and(final QueryPredicate... operands) {
        return and(asList(operands));
    }

    static QueryPredicate and(final List<QueryPredicate> operands) {
        return new AndPredicate(operands);
    }

    static QueryPredicate constTrue() {
        return new TruePredicate();
    }

    static QueryPredicate or(final QueryPredicate... operands) {
        return or(asList(operands));
    }

    static QueryPredicate or(final List<QueryPredicate> operands) {
        return new OrPredicate(operands);
    }

    static QueryPredicate eq(final QueryExpression e0, final QueryExpression e1) {
        return new EqualsPredicate(e0, e1);
    }
}
