package com.dajudge.buql.reflector.model;

import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodSelectModel<Q, R> {

    private final ReflectorPredicate predicate;
    private final String tableName;
    private final List<ResultField<R>> resultFields;
    private final Supplier<R> resultFactory;
    private final Function<Map<String, List<R>>, ? extends Object> postProcessor;

    public MethodSelectModel(
            final ReflectorPredicate predicate,
            final String tableName,
            final List<ResultField<R>> resultFields,
            final Supplier<R> resultFactory,
            final Function<Map<String, List<R>>, ? extends Object> postProcessor
    ) {
        this.predicate = predicate;
        this.tableName = tableName;
        this.resultFields = resultFields;
        this.resultFactory = resultFactory;
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

    public Supplier<R> getResultFactory() {
        return resultFactory;
    }

    public Function<Map<String, List<R>>, ? extends Object> getPostProcessor() {
        return postProcessor;
    }
}
