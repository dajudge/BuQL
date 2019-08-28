package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class FilterColExpression implements QueryExpression {
    private final String colName;

    public FilterColExpression(final String colName) {
        this.colName = colName;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.filterColumn(sources, this);
    }

    public String getColumnName() {
        return colName;
    }
}
