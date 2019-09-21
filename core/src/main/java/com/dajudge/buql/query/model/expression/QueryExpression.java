package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public interface QueryExpression {
    String toSql(ProjectionSources sources, final Dialect dialect);
}
