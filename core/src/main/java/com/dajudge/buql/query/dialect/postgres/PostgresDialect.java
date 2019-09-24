package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.SelectQuery;

public class PostgresDialect extends BaseDialect {
    @Override
    public QueryWithParameters select(final SelectQuery selectQuery) {
        final PostgresSelectQuerySerializer serializer = new PostgresSelectQuerySerializer(this, selectQuery);
        return new QueryWithParameters(serializer.toSql(), selectQuery.getFilterParameters());
    }
}
