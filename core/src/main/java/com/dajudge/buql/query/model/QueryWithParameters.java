package com.dajudge.buql.query.model;

import java.util.List;

public class QueryWithParameters {
    private final String sql;
    private final List<Object> queryParameters;

    public QueryWithParameters(final String sql, final List<Object> queryParameters) {
        this.sql = sql;
        this.queryParameters = queryParameters;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getQueryParameters() {
        return queryParameters;
    }
}
