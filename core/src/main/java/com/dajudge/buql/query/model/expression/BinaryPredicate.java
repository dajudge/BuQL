package com.dajudge.buql.query.model.expression;

public abstract class BinaryPredicate implements QueryPredicate {
    private final QueryExpression e0;
    private final QueryExpression e1;

    public BinaryPredicate(final QueryExpression e0, final QueryExpression e1) {
        this.e0 = e0;
        this.e1 = e1;
    }

    public QueryExpression getLeft() {
        return e0;
    }

    public QueryExpression getRight() {
        return e1;
    }
}
