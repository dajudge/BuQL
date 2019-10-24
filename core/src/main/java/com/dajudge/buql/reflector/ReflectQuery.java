package com.dajudge.buql.reflector;

import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.model.QueryModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ReflectQuery<Q, I, R> extends ReflectDatabaseOperation<Q, I, R> {


    protected ReflectQuery(
            final QueryModel<I, ?> query,
            final Function<Object, I> preProcessor,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        super(query, preProcessor, postProcessor);
    }

    @Override
    protected void executeQuery(
            final DatabaseEngine engine,
            final String sql,
            final List<Object> queryParameters,
            final DatabaseResultCallback currentCallback
    ) {
        engine.executeQuery(sql, queryParameters, currentCallback);
    }
}
