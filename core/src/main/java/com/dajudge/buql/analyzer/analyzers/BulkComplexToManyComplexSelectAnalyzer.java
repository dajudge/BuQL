package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.predicates.ComplexQueryTypePredicate;
import com.dajudge.buql.reflector.model.MethodSelectModelFactory.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dajudge.buql.analyzer.typeextractors.QueryTypeExtractors.extractFromComplexBulkMap;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToComplexManyMap;
import static java.util.function.Function.identity;

public class BulkComplexToManyComplexSelectAnalyzer extends BaseSelectAnalyzer {

    public BulkComplexToManyComplexSelectAnalyzer() {
        super(Pattern.compile("findBy[A-Z].*"));
    }

    @Override
    protected <R> ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final Matcher methodNameMatcher
    ) {
        return new ComplexResultTypeModel<>(clazz);
    }

    @Override
    protected <T> Function<Map<String, List<T>>, ?> createPostProcessor() {
        return identity();
    }

    @Override
    protected ReflectorPredicate createPredicate(
            final Method method,
            final Class<?> actualQueryClass,
            final Matcher methodNameMatcher
    ) {
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
}
