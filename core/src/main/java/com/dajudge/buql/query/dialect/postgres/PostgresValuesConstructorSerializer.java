package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.base.select.impl.ValuesConstructorSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

import static java.util.stream.Collectors.joining;

public class PostgresValuesConstructorSerializer extends ValuesConstructorSerializer {
    private final SelectQuery queryModel;
    private final String alias;

    public PostgresValuesConstructorSerializer(
            final SelectQuery queryModel,
            final String alias
    ) {
        super(queryModel);
        this.queryModel = queryModel;
        this.alias = alias;
    }

    @Override
    public String toSql() {
        return super.toSql() + " AS " + buildDeclaration();
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
