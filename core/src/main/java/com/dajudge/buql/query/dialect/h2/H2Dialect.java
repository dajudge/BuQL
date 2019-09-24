package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.SelectQuery;

public class H2Dialect extends BaseDialect {

    @Override
    public QueryWithParameters select(final SelectQuery selectQuery) {
        final H2SelectQuerySerializer serializer = new H2SelectQuerySerializer(selectQuery, this);
        return new QueryWithParameters(serializer.toSql(), selectQuery.getFilterParameters());
    }
}
