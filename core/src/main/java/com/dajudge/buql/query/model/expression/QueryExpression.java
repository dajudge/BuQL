package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public interface QueryExpression {
    static QueryExpression dataCol(final String colName) {
        return new DataColExpression(colName);
    }

    static QueryExpression filterCol(final String colName) {
        return new FilterColExpression(colName);
    }

    String toSql(ProjectionSources sources, final Dialect dialect);
}
