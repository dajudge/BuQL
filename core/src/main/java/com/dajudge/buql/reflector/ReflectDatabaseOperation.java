package com.dajudge.buql.reflector;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.model.QueryModel;
import com.dajudge.buql.query.model.QueryWithParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ReflectDatabaseOperation<Q, I, R> {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectDatabaseOperation.class);

    public Object postProcess(final Map<String, List<R>> result) {
        return postProcessor.apply(result);
    }

    public I preProcess(Object o) {
        return preProcessor.apply(o);
    }

    private final QueryModel<I, ?> queryModel;
    private final Function<Object, I> preProcessor;
    private final Function<Map<String, List<R>>, ?> postProcessor;

    protected ReflectDatabaseOperation(
            final QueryModel<I, ?> query,
            final Function<Object, I> preProcessor,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        this.queryModel = query;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
    }

    public void execute(
            final Dialect dialect,
            final DatabaseEngine engine,
            final I params,
            final Callback<R> callback
    ) {
        assert dialect != null : "dialect must not be null";
        assert engine != null : "engine must not be null";
        assert params != null : "params must not be null";
        assert callback != null : "callback must not be null";

        final List<QueryWithParameters> queries = queryModel.createQuery(params).toQueryBatch(dialect);
        assert !queries.isEmpty();
        executeQueries(engine, queries, callback);
    }

    private void executeQueries(
            final DatabaseEngine engine,
            final List<QueryWithParameters> remainingQueries,
            final Callback<R> callback
    ) {
        final Consumer<DatabaseResultCallback.ResultRow> resultRowConsumer = rowMapperFor(callback);
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
                resultRowConsumer.accept(row);
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
        executeQuery(engine, currentQuery.getSql(), currentQuery.getQueryParameters(), currentCallback);
    }

    protected abstract void executeQuery(
            DatabaseEngine engine,
            String sql,
            List<Object> queryParameters,
            DatabaseResultCallback currentCallback
    );

    protected abstract Consumer<DatabaseResultCallback.ResultRow> rowMapperFor(final Callback<R> callback);

    public abstract Callback<R> createCallback(final I params);

    public interface Callback<R> {
        void onResult(final String id, final R value);

        void done();

        void onError(final Exception e);

        Map<String, List<R>> getResult();

        static <T> Callback<T> nullCallback() {
            return new Callback<T>() {
                @Override
                public void onResult(final String id, final T value) {

                }

                @Override
                public void done() {

                }

                @Override
                public void onError(final Exception e) {

                }

                @Override
                public Map<String, List<T>> getResult() {
                    return null;
                }
            };
        }
    }
}
