package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;

public class ConstructorFilterTableSerializer implements FilterTableSerializer {

    private final ValuesConstructorSerializer valuesConstructorSerializer;

    public ConstructorFilterTableSerializer(
            final ValuesConstructorSerializer valuesConstructorSerializer
    ) {
        this.valuesConstructorSerializer = valuesConstructorSerializer;
    }

    @Override
    public String getSql() {
        return valuesConstructorSerializer.toSql();
    }

    @Override
    public String referenceField(final String columnName) {
        return valuesConstructorSerializer.referenceField(columnName);
    }
}
