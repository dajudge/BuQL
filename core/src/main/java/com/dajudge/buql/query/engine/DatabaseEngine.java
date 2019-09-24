package com.dajudge.buql.query.engine;

import java.util.List;

public interface DatabaseEngine {
    void executeQuery(final String query, final List<Object> params, final DatabaseResultCallback cb);

    void executeStatement(
            String sql,
            List<? extends Object> params,
            DatabaseResultCallback cb
    );
}
