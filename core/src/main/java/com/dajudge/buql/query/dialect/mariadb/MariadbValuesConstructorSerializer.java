package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.dialect.base.select.impl.ValuesConstructorSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

import static java.util.stream.Collectors.joining;

public class MariadbValuesConstructorSerializer extends ValuesConstructorSerializer {
    private final SelectQuery queryModel;
    private final String alias;

    public MariadbValuesConstructorSerializer(
            final SelectQuery queryModel,
            final String alias
    ) {
        super(queryModel);
        this.queryModel = queryModel;
        this.alias = alias;
    }

    @Override
    public String toSql() {
        return buildDeclaration() + " AS " + super.toSql();
    }

    @Override
    public String referenceField(final String filterColumn) {
        return alias + "." + filterColumn;
    }

    private String buildDeclaration() {
        final String cols = queryModel.getFilterColumns().stream().collect(joining(","));
        return this.alias + "(" + cols + ")";
    }
}
