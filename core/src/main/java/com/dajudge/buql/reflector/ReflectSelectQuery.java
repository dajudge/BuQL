package com.dajudge.buql.reflector;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.SelectQueryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

public class ReflectSelectQuery<Q, R> {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectSelectQuery.class);

    public interface Callback<R> {
        void onResult(final String id, final R value);

        void done();

        void onError(final Exception e);
    }

    private final SelectQueryModel<Map<String, Q>> queryModel;
    private final ResultMapper<R> resultMapper;

    public ReflectSelectQuery(
            final SelectQueryModel<Map<String, Q>> query,
            final ResultMapper<R> resultMapper
    ) {
        this.queryModel = query;
        this.resultMapper = resultMapper;
    }

    public void execute(
            final Dialect dialect,
            final DatabaseEngine engine,
            final Map<String, Q> params,
            final Callback<R> callback
    ) {
        assert dialect != null;
        assert engine != null;
        assert params != null;
        assert callback != null;

        final QueryWithParameters query = queryModel.create(params).toSelectQuery(dialect);
        engine.executeQuery(query.getSql(), query.getQueryParameters(), new DefaultDatabaseResultCallback() {
            @Override
            public void onMetadata(final ResultMetadata metadata) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Result columns: {}", metadata.getColumnNames());
                }
            }

            @Override
            public void onRow(final ResultRow row) {
                callback.onResult(
                        resultMapper.getId(row::getObject),
                        resultMapper.getResultObject(row::getObject)
                );
            }

            @Override
            public void onError(final SQLException e) {
                callback.onError(e);
            }

            @Override
            public void done() {
                callback.done();
            }
        });
    }
}
