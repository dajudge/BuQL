package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public enum QueryMultiplicity {
    ONE(SingleQueryPreProcessor::new, new SingleResultPostProcessor(), Function.identity()),
    MANY(BulkQueryPreProcessor::new, i -> i, ReflectionUtil::mapOfStringTo);

    private final QueryPreprocessorFactory queryPreProcessorFactory;
    private final Function<Map<String, ?>, ?> queryPostprocessorFactory;
    private final Function<Predicate<Type>, Predicate<Type>> transform;

    QueryMultiplicity(
            final QueryPreprocessorFactory queryPreProcessorFactory,
            final Function<Map<String, ?>, ?> queryPostprocessorFactory,
            final Function<Predicate<Type>, Predicate<Type>> transform) {
        this.queryPreProcessorFactory = queryPreProcessorFactory;
        this.queryPostprocessorFactory = queryPostprocessorFactory;
        this.transform = transform;
    }

    public Function<Map<String, ?>, ?> getPostProcessor() {
        return queryPostprocessorFactory;
    }

    public Function<Predicate<Type>, Predicate<Type>> getTransform() {
        return transform;
    }

    public QueryPreprocessorFactory getPreProcessorFactory() {
        return queryPreProcessorFactory;
    }
}
