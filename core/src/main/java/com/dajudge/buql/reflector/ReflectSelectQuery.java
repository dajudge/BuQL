package com.dajudge.buql.reflector;

import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.model.select.SelectQueryModel;
import com.dajudge.buql.util.CollectorCallback;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReflectSelectQuery<Q, R> extends ReflectQuery<Q, Map<String, Q>, R> {
    private final ResultMapper<R> resultMapper;

    public ReflectSelectQuery(
            final SelectQueryModel<Map<String, Q>> queryModel,
            final ResultMapper<R> resultMapper,
            final Function<Object, Map<String, Q>> preProcessor,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        super(queryModel, preProcessor, postProcessor);
        this.resultMapper = resultMapper;
    }

    @Override
    protected Consumer<DatabaseResultCallback.ResultRow> rowMapperFor(final Callback<R> callback) {
        return row -> callback.onResult(
                resultMapper.getId(row::getObject),
                resultMapper.getResultObject(row::getObject)
        );
    }

    @Override
    public Callback<R> createCallback(final Map<String, Q> params) {
        return new CollectorCallback<>(params.keySet());
    }
}
