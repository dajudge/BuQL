package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;
import com.dajudge.buql.query.dialect.base.select.PredicateSerializer;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.query.model.select.SelectQuery;

public class DefaultPredicateSerializer implements PredicateSerializer {
    private final ProjectionColumn.ProjectionSources sources;
    private final SelectQuery selectQuery;
    private final Dialect dialect;

    public DefaultPredicateSerializer(
            final FilterTableSerializer filterTable,
            final DataTableSerializer dataTable,
            final SelectQuery selectQuery,
            final Dialect dialect
    ) {
        this.selectQuery = selectQuery;
        this.dialect = dialect;
        sources = new QueryProjectionSources(filterTable, dataTable);
    }

    @Override
    public String getSql() {
        return selectQuery.getPredicate().toSql(sources, dialect);
    }
}
