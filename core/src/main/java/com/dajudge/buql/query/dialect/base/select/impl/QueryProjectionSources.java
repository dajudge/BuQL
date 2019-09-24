package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;
import com.dajudge.buql.query.model.select.ProjectionColumn;

import java.util.function.Function;

public class QueryProjectionSources implements ProjectionColumn.ProjectionSources {
    private final FilterTableSerializer filterTable;
    private final DataTableSerializer dataTable;

    public QueryProjectionSources(final FilterTableSerializer filterTable, final DataTableSerializer dataTable) {
        this.filterTable = filterTable;
        this.dataTable = dataTable;
    }

    @Override
    public String getFiltersColumn(final String filterColumn) {
        return ((Function<String, String>) filterTable::referenceField).apply(filterColumn);
    }

    @Override
    public String getDataColumn(final String dataColumnName) {
        return ((Function<String, String>) dataTable::referenceField).apply(dataColumnName);
    }
}
