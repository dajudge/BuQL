package com.dajudge.buql.query.engine;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.Query;
import com.dajudge.buql.query.model.QueryWithParameters;

public class QueryExecutor {
    private final DatabaseEngine engine;
    private final Dialect dialect;

    public QueryExecutor(final DatabaseEngine engine, final Dialect dialect) {
        this.engine = engine;
        this.dialect = dialect;
    }

    public void execute(final Query query, final DatabaseResultCallback cb) {
        final QueryWithParameters rawQuery = query.toSelectQuery(dialect);
        engine.executeQuery(rawQuery.getSql(), rawQuery.getQueryParameters(), cb);
    }
}
