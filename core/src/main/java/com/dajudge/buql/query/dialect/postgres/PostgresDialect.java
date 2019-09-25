package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.SelectQuery;

public class PostgresDialect extends BaseDialect {
    @Override
    public QueryWithParameters selectInternal(final SelectQuery selectQuery) {
        final PostgresSelectQuerySerializer serializer = new PostgresSelectQuerySerializer(this, selectQuery);
        return new QueryWithParameters(serializer.toSql(), selectQuery.getFilterParameters());
    }

    @Override
    protected int getMaxParamsPerQuery() {
        return Short.MAX_VALUE; // https://stackoverflow.com/questions/1009706/postgresql-max-number-of-parameters-in-in-clause
    }
}
