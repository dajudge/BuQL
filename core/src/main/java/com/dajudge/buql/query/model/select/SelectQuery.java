package com.dajudge.buql.query.model.select;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.Query;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.QueryPredicate;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SelectQuery implements Query {

    private final List<ProjectionColumn> projectionColumns;
    private final List<Object> filterParameters;
    private final List<String> filterColumns;
    private final QueryPredicate predicate;
    private final String queryTable;

    public SelectQuery(
            final List<ProjectionColumn> projectionColumns,
            final List<Object> filterParameters,
            final List<String> filterColumns,
            final QueryPredicate predicate,
            final String queryTable
    ) {
        assert !projectionColumns.isEmpty() : "projectionColumns must not be empty";
        assert !filterColumns.isEmpty() : "filterColumms must not be empty";
        assert 0 == filterParameters.size() % filterColumns.size()
                : "size of filterParameters must be a multiple of the size of filterColumns";
        this.projectionColumns = unmodifiableList(projectionColumns);
        this.filterParameters = unmodifiableList(filterParameters);
        this.filterColumns = unmodifiableList(filterColumns);
        this.queryTable = queryTable;
        this.predicate = predicate;
    }

    public int getParamsPerFilterRow() {
        return filterColumns.size();
    }

    public List<Object> getFilterParameters() {
        return filterParameters;
    }

    public List<ProjectionColumn> getProjectionColumns() {
        return projectionColumns;
    }

    @Override
    public QueryWithParameters toSelectQuery(final Dialect dialect) {
        return dialect.select(this);
    }

    public int getFilterRowCount() {
        return filterParameters.size() / getParamsPerFilterRow();
    }

    public String getQueryTable() {
        return queryTable;
    }

    public List<String> getFilterColumns() {
        return filterColumns;
    }

    public QueryPredicate getPredicate() {
        return predicate;
    }
}
