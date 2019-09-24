package com.dajudge.buql.query.dialect.base.select.impl;

import java.util.List;

public interface ValuesWriter {
    String writeValues(
            final List<String> filterColumns,
            final int paramsPerFilterRow,
            final int filterRowCount
    );

    String prefix(String filterColumn);
}
