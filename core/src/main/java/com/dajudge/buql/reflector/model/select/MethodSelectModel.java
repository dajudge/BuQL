package com.dajudge.buql.reflector.model.select;

import com.dajudge.buql.reflector.model.MethodModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MethodSelectModel<Q, R> extends MethodModel {

    private final ReflectorPredicate predicate;
    private final List<ResultField<R>> resultFields;
    private final Function<Object, Map<String, Q>> preProcessor;
    private final Function<Map<String, List<R>>, ?> postProcessor;
    private final Function<Function<String, Object>, R> resultTypeFactory;

    public MethodSelectModel(
            final ReflectorPredicate predicate,
            final String tableName,
            final List<ResultField<R>> resultFields,
            final Function<Function<String, Object>, R> resultTypeFactory,
            final Function<Object, Map<String, Q>> preProcessor,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        super(tableName);
        this.predicate = predicate;
        this.resultFields = resultFields;
        this.resultTypeFactory = resultTypeFactory;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
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
