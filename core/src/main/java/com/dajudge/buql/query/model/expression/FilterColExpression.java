package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

public class FilterColExpression implements QueryExpression {
    private final String colName;

    public FilterColExpression(final String colName) {
        this.colName = colName;
    }

    public String getColumnName() {
        return colName;
    }

    @Override
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.filterCol(this);
    }
}
