package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.SelectQuery;

public class MariadbDialect extends BaseDialect {
    @Override
    public QueryWithParameters selectInternal(final SelectQuery selectQuery) {
        final MariadbWithSelectQuerySerializer serializer = new MariadbWithSelectQuerySerializer(this, selectQuery);
        return new QueryWithParameters(
                serializer.toSql(),
                selectQuery.getFilterParameters()
        );
    }

    @Override
    protected int getMaxParamsPerQuery() {
        return 65535; // https://stackoverflow.com/questions/4922345/how-many-bind-variables-can-i-use-in-a-sql-query-in-mysql-5/11131824./
    }
}
