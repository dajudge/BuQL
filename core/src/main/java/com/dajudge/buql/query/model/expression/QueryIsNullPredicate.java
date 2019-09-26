package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

public class QueryIsNullPredicate implements QueryPredicate {
    private final QueryExpression expression;

    public QueryIsNullPredicate(final QueryExpression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.isNull(expression);
    }
}
