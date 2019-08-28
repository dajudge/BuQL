package com.dajudge.buql.query.dialect;

import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public abstract class SelectQueryWriter {

    private final Dialect dialect;
    private final ValuesWriter valuesWriter;
    private final Supplier<ColRenamer> filterColRenamerFactory;
    private final Supplier<ColRenamer> dataColRenamerFactory;

    public SelectQueryWriter(
            final Dialect dialect,
            final ValuesWriter valuesWriter,
            final Supplier<ColRenamer> filterColRenamerFactory,
            final Supplier<ColRenamer> dataColRenamerFactory
    ) {
        this.dialect = dialect;
        this.valuesWriter = valuesWriter;
        this.filterColRenamerFactory = filterColRenamerFactory;
        this.dataColRenamerFactory = dataColRenamerFactory;
    }

    private String buildProjection(final SelectQuery selectQuery, final ProjectionSources sources) {
        final List<ProjectionColumn> columns = selectQuery.getProjectionColumns();
        return columns.stream()
                .map(c -> c.getSourceColumn(sources) + " AS " + c.getLabel())
                .collect(joining(", "));
    }


    protected abstract ProjectionSources sources(final ColRenamer filterColRenamer, final ColRenamer dataColRenamer);

    public QueryWithParameters toSql(final SelectQuery selectQuery) {
        final ColRenamer filterColRenamer = filterColRenamerFactory.get();
        final ColRenamer dataColRenamer = dataColRenamerFactory.get();
        final ProjectionSources sources = sources(filterColRenamer, dataColRenamer);
        final String filterValues = buildFilterValues(selectQuery, filterColRenamer);
        final String table = selectQuery.getQueryTable();
        final String predicate = buildPredicate(selectQuery, sources);
        final String projection = buildProjection(selectQuery, sources);
        final String sql = "SELECT " + projection + " FROM " + filterValues + ", " + table + " D WHERE " + predicate;
        return new QueryWithParameters(sql, selectQuery.getFilterParameters());
    }

    private String buildPredicate(final SelectQuery selectQuery, final ProjectionSources sources) {
        return selectQuery.getPredicate().toSql(sources, dialect);
    }

    private String buildFilterValues(final SelectQuery selectQuery, final ColRenamer filterColRenamer) {
        final int paramsPerFilterRow = selectQuery.getParamsPerFilterRow();
        final int filterRowCount = selectQuery.getFilterRowCount();
        final List<String> filterColumns = selectQuery.getFilterColumns().stream()
                .map(c -> filterColRenamer.rename(c))
                .collect(Collectors.toList());
        return valuesWriter.writeValues(filterColumns, paramsPerFilterRow, filterRowCount);
    }
}
