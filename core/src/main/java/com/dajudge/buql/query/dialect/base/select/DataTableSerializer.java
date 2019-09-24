package com.dajudge.buql.query.dialect.base.select;

public interface DataTableSerializer {
    String getSql();

    String referenceField(String columnName);
}
