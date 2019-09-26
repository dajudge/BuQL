package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

public class TruePredicate implements QueryPredicate {
    @Override
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.constantTrue();
    }
}
