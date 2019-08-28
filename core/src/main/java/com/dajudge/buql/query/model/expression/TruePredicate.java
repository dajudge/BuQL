package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn;

public class TruePredicate implements QueryPredicate {
    @Override
    public String toSql(final ProjectionColumn.ProjectionSources sources, final Dialect dialect) {
        return dialect.constTrue();
    }
}
