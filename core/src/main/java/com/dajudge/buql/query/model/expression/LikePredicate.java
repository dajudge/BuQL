package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class LikePredicate extends BinaryPredicate {

    public LikePredicate(final QueryExpression e0, final QueryExpression e1) {
        super(e0, e1);
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.like(sources, this);
    }
}
