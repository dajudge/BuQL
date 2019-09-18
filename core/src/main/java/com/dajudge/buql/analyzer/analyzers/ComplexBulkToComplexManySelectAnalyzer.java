package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.ComplexQueryTypePredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.dajudge.buql.analyzer.typeextractors.QueryTypeExtractors.extractFromComplexBulkMap;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToComplexManyMap;
import static java.util.function.Function.identity;

public class ComplexBulkToComplexManySelectAnalyzer extends BaseSelectAnalyzer {

    @Override
    protected <T> Function<Map<String, List<T>>, ?> getPostProcessor() {
        return identity();
    }

    @Override
    protected ReflectorPredicate toPredicate(final Method method, final Class<?> actualQueryClass) {
        return new ComplexQueryTypePredicate(actualQueryClass);
    }

    @Override
    protected Optional<Type> extractResultType(final Method method) {
        return extractToComplexManyMap(method);
    }

    @Override
    protected Optional<Type> extractQueryType(final Method method) {
        return extractFromComplexBulkMap(method);
    }

    @Override
    protected boolean matchesName(final Method method) {
        return method.getName().matches("findBy[A-Z].*");
    }
}