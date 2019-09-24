package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.model.select.SelectQuery;

import static com.dajudge.buql.util.StringUtils.repeat;

public abstract class ValuesConstructorSerializer {
    private final SelectQuery queryModel;

    public ValuesConstructorSerializer(final SelectQuery queryModel) {
        this.queryModel = queryModel;
    }

    public String toSql() {
        final String row = "(" + repeat("?", ", ", queryModel.getParamsPerFilterRow()) + ")";
        return "(VALUES " + repeat(row, ", ", queryModel.getFilterRowCount()) + ")";
    }

    public abstract String referenceField(final String filterColumn);
}
