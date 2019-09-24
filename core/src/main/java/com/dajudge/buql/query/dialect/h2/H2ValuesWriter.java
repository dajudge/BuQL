package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.base.select.impl.ValuesWriter;

import java.util.List;

import static com.dajudge.buql.util.StringUtils.repeat;

public class H2ValuesWriter implements ValuesWriter {

    public String writeValues(
            final List<String> filterColumns,
            final int paramsPerFilterRow,
            final int filterRowCount
    ) {
        final String row = "(" + repeat("?", ", ", paramsPerFilterRow) + ")";
        return "(VALUES " + repeat(row, ", ", filterRowCount) + ")";
    }

    @Override
    public String prefix(final String filterColumn) {
        return filterColumn;
    }
}
