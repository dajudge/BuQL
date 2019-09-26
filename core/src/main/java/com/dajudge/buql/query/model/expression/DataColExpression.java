package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

public class DataColExpression implements QueryExpression {
    private final String colName;

    public DataColExpression(final String colName) {
        this.colName = colName;
    }

    public String getColumnName() {
        return colName;
    }

    @Override
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.dataCol(this);
    }
}
