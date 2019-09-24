package com.dajudge.buql.query.dialect.base.select;

public interface FilterTableSerializer {
    String getSql();

    String referenceField(String columnName);
}
