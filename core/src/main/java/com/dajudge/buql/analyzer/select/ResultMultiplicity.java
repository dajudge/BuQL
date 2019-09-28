package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.ReflectionUtil;
import com.dajudge.buql.analyzer.UniquePostProcessor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

public enum ResultMultiplicity {
    ONE("get", UniquePostProcessor::new, identity()),
    MANY("find", new ResultTypePostProcessorFactory() {
        @Override
        public <T> Function<Map<String, List<T>>, Map<String, ?>> getPostProcessor() {
            return i -> i;
        }
    }, ReflectionUtil::listOf);

    private final String prefix;
    private final ResultTypePostProcessorFactory factory;
    private final Function<Predicate<Type>, Predicate<Type>> transform;

    ResultMultiplicity(
            final String prefix,
            final ResultTypePostProcessorFactory factory,
            final Function<Predicate<Type>, Predicate<Type>> transform
    ) {
        this.prefix = prefix;
        this.factory = factory;
        this.transform = transform;
    }

    String getPatternPrefix() {
        return prefix;
    }

    public <T> Function<Map<String, List<T>>, Map<String, ?>> getPostProcessor() {
        return factory.getPostProcessor();
    }

    public Function<Predicate<Type>, Predicate<Type>> getTransform() {
        return transform;
    }
}
