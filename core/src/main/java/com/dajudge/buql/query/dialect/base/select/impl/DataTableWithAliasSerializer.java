package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

public class DataTableWithAliasSerializer implements DataTableSerializer {
    private final SelectQuery selectQuery;
    private final String alias;

    public DataTableWithAliasSerializer(final SelectQuery selectQuery, final String alias) {
        this.selectQuery = selectQuery;
        this.alias = alias;
    }

    @Override
    public String getSql() {
        return selectQuery.getQueryTable() + " " + alias;
    }

    @Override
    public String referenceField(String columnName) {
        return alias + "." + columnName;
    }
}
