package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.BulkPreProcessor;
import com.dajudge.buql.analyzer.PrimitiveResultTypeModel;
import com.dajudge.buql.analyzer.UniquePostProcessor;
import com.dajudge.buql.analyzer.predicates.ComplexQueryTypePredicate;
import com.dajudge.buql.reflector.model.MethodSelectModelFactory;
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
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToPrimitiveUniqueMap;
import static com.dajudge.buql.util.StringUtils.lowercaseFirstLetter;

public class BulkComplexToUniquePrimitiveSelectAnalyzer extends BaseSelectAnalyzer {

    public BulkComplexToUniquePrimitiveSelectAnalyzer() {
        super(Pattern.compile("get([A-Z].*)By[A-Z].*"));
    }

    @Override
    protected <T> Function<Map<String, List<T>>, ?> createPostProcessor() {
        return new UniquePostProcessor<>();
    }

    @Override
    protected <Q> Function<Object, Map<String, Q>> createPreProcessor() {
        return new BulkPreProcessor<>();
    }

    @Override
    protected <R> MethodSelectModelFactory.ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final Matcher methodNameMatcher
    ) {
        return new PrimitiveResultTypeModel<>(lowercaseFirstLetter(methodNameMatcher.group(1)));
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
        return extractToPrimitiveUniqueMap(method);
    }

    @Override
    protected Optional<Type> extractQueryType(final Method method) {
        return extractFromComplexBulkMap(method);
    }
}
