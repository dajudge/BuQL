package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class EqualsPredicate implements QueryPredicate {
    private final QueryExpression e0;
    private final QueryExpression e1;

    public EqualsPredicate(final QueryExpression e0, final QueryExpression e1) {
        this.e0 = e0;
        this.e1 = e1;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.eq(sources, this);
    }

    public QueryExpression getLeft() {
        return e0;
    }

    public QueryExpression getRight() {
        return e1;
    }
}
