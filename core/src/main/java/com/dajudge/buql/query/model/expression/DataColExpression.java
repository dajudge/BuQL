package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class DataColExpression implements QueryExpression {
    private final String colName;

    public DataColExpression(final String colName) {
        this.colName = colName;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.dataColumn(sources, this);
    }

    public String getColumnName() {
        return colName;
    }
}
