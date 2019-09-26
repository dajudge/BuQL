package com.dajudge.buql.reflector;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.SelectQueryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ReflectSelectQuery<Q, R> {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectSelectQuery.class);

    public Object postProcess(final Map<String, List<R>> result) {
        return postProcessor.apply(result);
    }

    public Map<String, Q> preProcess(Object o) {
        return preProcessor.apply(o);
    }

    public interface Callback<R> {
        void onResult(final String id, final R value);

        void done();

        void onError(final Exception e);
    }

    private final SelectQueryModel<Map<String, Q>> queryModel;
    private final ResultMapper<R> resultMapper;
    private final Function<Object, Map<String, Q>> preProcessor;
    private final Function<Map<String, List<R>>, ? extends Object> postProcessor;

    public ReflectSelectQuery(
            final SelectQueryModel<Map<String, Q>> query,
            final ResultMapper<R> resultMapper,
            final Function<Object, Map<String, Q>> preProcessor,
            final Function<Map<String, List<R>>, ? extends Object> postProcessor
    ) {
        this.queryModel = query;
        this.resultMapper = resultMapper;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
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

        final List<QueryWithParameters> queries = queryModel.create(params).toSelectQuery(dialect);
        assert !queries.isEmpty();
        executeQueries(engine, queries, callback);
    }

    private void executeQueries(
            final DatabaseEngine engine,
            final List<QueryWithParameters> remainingQueries,
            final Callback<R> callback
    ) {
        final QueryWithParameters currentQuery = remainingQueries.get(0);
        final DatabaseResultCallback currentCallback = new DefaultDatabaseResultCallback() {
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
                if (remainingQueries.size() == 1) {
                    callback.done();
                } else {
                    executeQueries(engine, remainingQueries.subList(1, remainingQueries.size()), callback);
                }
            }
        };
        engine.executeQuery(currentQuery.getSql(), currentQuery.getQueryParameters(), currentCallback);
    }
}
