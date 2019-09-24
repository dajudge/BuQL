package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.model.select.SelectQuery;

public class MariadbWithSelectQuerySerializer {
    private final MariadbDialect dialect;
    private final SelectQuery queryModel;

    public MariadbWithSelectQuerySerializer(final MariadbDialect dialect, final SelectQuery queryModel) {
        this.dialect = dialect;
        this.queryModel = queryModel;
    }

    public String toSql() {
        final MariadbSelectQuerySerializer selectSerializer = new MariadbSelectQuerySerializer(dialect, queryModel, "F");
        final MariadbValuesConstructorSerializer constructorSerializer = new MariadbValuesConstructorSerializer(queryModel, "F");
        return "WITH " + constructorSerializer.toSql() + " " + selectSerializer.toSql();
    }
}
