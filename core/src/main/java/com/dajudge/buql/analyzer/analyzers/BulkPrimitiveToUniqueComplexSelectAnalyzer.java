package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.UniquePostProcessor;
import com.dajudge.buql.analyzer.predicates.PrimitiveQueryTypePredicate;
import com.dajudge.buql.reflector.model.MethodSelectModelFactory;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;

import static com.dajudge.buql.analyzer.typeextractors.QueryTypeExtractors.extractFromPrimitiveBulkMap;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToComplexUniqueMap;
import static com.dajudge.buql.util.StringUtils.lowercaseFirstLetter;
import static java.util.regex.Pattern.compile;

public class BulkPrimitiveToUniqueComplexSelectAnalyzer extends BaseSelectAnalyzer {

    public BulkPrimitiveToUniqueComplexSelectAnalyzer() {
        super(compile("getBy([A-Z].*)"));
    }

    @Override
    protected <T> Function<Map<String, List<T>>, ?> createPostProcessor() {
        return new UniquePostProcessor<>();
    }

    @Override
    protected <R> MethodSelectModelFactory.ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final Matcher methodNameMatcher
    ) {
        return new ComplexResultTypeModel<>(clazz);
    }

    @Override
    protected ReflectorPredicate createPredicate(
            final Method method,
            final Class<?> actualQueryType,
            final Matcher methodNameMatcher
    ) {
        return new PrimitiveQueryTypePredicate(lowercaseFirstLetter(methodNameMatcher.group(1)));
    }

    @Override
    protected Optional<Type> extractResultType(final Method method) {
        return extractToComplexUniqueMap(method);
    }

    @Override
    protected Optional<Type> extractQueryType(final Method method) {
        return extractFromPrimitiveBulkMap(method);
    }
}
