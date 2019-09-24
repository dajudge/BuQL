package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;
import com.dajudge.buql.query.dialect.base.select.ProjectionSerializer;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.query.model.select.SelectQuery;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class DefaultProjectionSerializer implements ProjectionSerializer {
    private final ProjectionColumn.ProjectionSources sources;
    private final SelectQuery selectQuery;

    public DefaultProjectionSerializer(
            final FilterTableSerializer filterTable,
            final DataTableSerializer dataTable,
            final SelectQuery selectQuery
    ) {
        this.selectQuery = selectQuery;
        sources = new QueryProjectionSources(filterTable, dataTable);
    }

    @Override
    public String getSql() {
        final List<ProjectionColumn> columns = selectQuery.getProjectionColumns();
        return columns.stream()
                .map(c -> c.getSourceColumn(sources) + " AS " + c.getLabel())
                .collect(joining(", "));
    }
}
