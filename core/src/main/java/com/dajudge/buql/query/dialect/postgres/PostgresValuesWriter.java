package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.base.select.impl.ValuesWriter;

import java.util.List;

import static com.dajudge.buql.util.StringUtils.repeat;
import static java.util.stream.Collectors.joining;

public class PostgresValuesWriter implements ValuesWriter {
    private final String alias;

    public PostgresValuesWriter(final String alias) {
        this.alias = alias;
    }

    public String writeValues(
            final List<String> filterColumns,
            final int paramsPerFilterRow,
            final int filterRowCount
    ) {
        final String row = "(" + repeat("?", ", ", paramsPerFilterRow) + ")";
        final String cols = filterColumns.stream().collect(joining(","));
        final String alias = " AS " + this.alias + "(" + cols + ")";
        return "(VALUES " + repeat(row, ", ", filterRowCount) + ")" + alias;
    }

    @Override
    public String prefix(final String filterColumn) {
        return alias + "." + filterColumn;
    }

}
