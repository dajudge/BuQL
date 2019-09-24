package com.dajudge.buql.query.dialect.base.select;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.base.select.impl.DefaultPredicateSerializer;
import com.dajudge.buql.query.dialect.base.select.impl.DefaultProjectionSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

public abstract class BaseSelectQuerySerializer {
    private final Dialect dialect;
    private final SelectQuery queryModel;

    protected BaseSelectQuerySerializer(final Dialect dialect, final SelectQuery queryModel) {
        this.dialect = dialect;
        this.queryModel = queryModel;
    }

    public String toSql() {
        final FilterTableSerializer filterTable = getFilterTable();
        final DataTableSerializer dataTable = getDataTable();
        final String datasourcesSql = "FROM " + filterTable.getSql() + ", " + dataTable.getSql();
        final String projectionSql = "SELECT " + getProjection(filterTable, dataTable).getSql();
        final String conditionSql = "WHERE " + getPredicate(filterTable, dataTable).getSql();
        return projectionSql + " " + datasourcesSql + " " + conditionSql;
    }

    protected ProjectionSerializer getProjection(
            final FilterTableSerializer filterTable,
            final DataTableSerializer dataTable
    ) {
        return new DefaultProjectionSerializer(filterTable, dataTable, queryModel);
    }

    protected PredicateSerializer getPredicate(
            final FilterTableSerializer filterTable,
            final DataTableSerializer dataTable
    ) {
        return new DefaultPredicateSerializer(filterTable, dataTable, queryModel, dialect);
    }

    protected abstract DataTableSerializer getDataTable();

    protected abstract FilterTableSerializer getFilterTable();
}
