package com.dajudge.buql.reflector.model;

import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MethodSelectModel<Q, R> {

    private final ReflectorPredicate predicate;
    private final String tableName;
    private final List<ResultField<R>> resultFields;
    private final Function<Object, Map<String, Q>> preProcessor;
    private final Function<Map<String, List<R>>, ? extends Object> postProcessor;
    private final Function<Function<String, Object>, R> resultTypeFactory;

    public MethodSelectModel(
            final ReflectorPredicate predicate,
            final String tableName,
            final List<ResultField<R>> resultFields,
            final Function<Function<String, Object>, R> resultTypeFactory,
            final Function<Object, Map<String, Q>> preProcessor,
            final Function<Map<String, List<R>>, ? extends Object> postProcessor
    ) {
        this.predicate = predicate;
        this.tableName = tableName;
        this.resultFields = resultFields;
        this.resultTypeFactory = resultTypeFactory;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ResultField<R>> getResultFields() {
        return resultFields;
    }

    public ReflectorPredicate getPredicate() {
        return predicate;
    }

    public Function<Map<String, List<R>>, ? extends Object> getPostProcessor() {
        return postProcessor;
    }

    public Function<Function<String, Object>, R> getResultTypeFactory() {
        return resultTypeFactory;
    }

    public Function<Object, Map<String, Q>> getPreProcessor() {
        return preProcessor;
    }
}
