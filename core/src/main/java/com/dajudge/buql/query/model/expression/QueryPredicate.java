package com.dajudge.buql.query.model.expression;

import java.util.List;

public interface QueryPredicate extends QueryExpression {
    static QueryPredicate and(final List<QueryPredicate> operands) {
        return new AndPredicate(operands);
    }

    static QueryPredicate constTrue() {
        return new TruePredicate();
    }

    static QueryPredicate or(final List<QueryPredicate> operands) {
        return new OrPredicate(operands);
    }

    static QueryPredicate compare(
            final QueryExpression e0,
            final QueryExpression e1,
            final QueryCompareOperator operator
    ) {
        return new QueryComparePredicate(e0, e1, operator);
    }
}
