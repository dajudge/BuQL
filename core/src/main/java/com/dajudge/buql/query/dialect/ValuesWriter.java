package com.dajudge.buql.query.dialect;

import java.util.List;

import static com.dajudge.buql.util.StringUtils.repeat;
import static java.util.stream.Collectors.joining;

public class ValuesWriter {
    private final String aliasName;

    public ValuesWriter(final String alias) {
        this.aliasName = alias;
    }

    public String writeValues(
            final List<String> filterColumns,
            final int paramsPerFilterRow,
            final int filterRowCount
    ) {
        final String row = "(" + repeat("?", ", ", paramsPerFilterRow) + ")";
        final String alias = aliasName == null ? "" : buildAlias(aliasName, filterColumns);
        return "(VALUES " + repeat(row, ", ", filterRowCount) + ")" + alias;
    }

    private String buildAlias(
            final String aliasName,
            final List<String> filterColumns
    ) {
        final String cols = filterColumns.stream().collect(joining(","));
        return " AS " + aliasName + "(" + cols + ")";
    }
}
