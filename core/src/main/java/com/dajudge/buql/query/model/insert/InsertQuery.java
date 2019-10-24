package com.dajudge.buql.query.model.insert;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.Query;
import com.dajudge.buql.query.model.QueryWithParameters;

import java.util.List;
import java.util.Map;

public class InsertQuery implements Query {
    private final List<Map<String, Object>> insertRows;
    private final String table;

    public InsertQuery(final String table, final List<Map<String, Object>> insertRows) {
        this.table = table;
        this.insertRows = insertRows;
    }

    @Override
    public List<QueryWithParameters> toQueryBatch(final Dialect dialect) {
        return dialect.insert(this);
    }

    public List<Map<String, Object>> getInsertRows() {
        return insertRows;
    }

    public String getTable() {
        return table;
    }
}
