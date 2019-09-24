package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.select.SelectQuery;

public class MariadbDialect extends BaseDialect {
    @Override
    public QueryWithParameters select(final SelectQuery selectQuery) {
        final MariadbWithSelectQuerySerializer serializer = new MariadbWithSelectQuerySerializer(this, selectQuery);
        return new QueryWithParameters(
                serializer.toSql(),
                selectQuery.getFilterParameters()
        );
    }
}
