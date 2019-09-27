package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.PrimitiveResultTypeModel;
import com.dajudge.buql.analyzer.SingleQueryPreProcessor;
import com.dajudge.buql.analyzer.SingleResultPostProcessor;
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

import static com.dajudge.buql.analyzer.typeextractors.QueryTypeExtractors.extractFromSinglePrimitive;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToComplexMany;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToPrimitiveMany;
import static com.dajudge.buql.util.StringUtils.lowercaseFirstLetter;
import static java.util.regex.Pattern.compile;

public class SinglePrimitiveToManyPrimitiveSelectAnalyzer extends BaseSelectAnalyzer {

    private static final String RESULT_ID = "ID";

    public SinglePrimitiveToManyPrimitiveSelectAnalyzer() {
        super(compile("find([A-Z].*)By([A-Z].*)"));
    }

    @Override
    protected <T> Function<Map<String, List<T>>, ?> createPostProcessor() {
        return new SingleResultPostProcessor<>(RESULT_ID);
    }

    @Override
    protected <Q> Function<Object, Map<String, Q>> createPreProcessor() {
        return new SingleQueryPreProcessor<>(RESULT_ID);
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
            final Class<?> actualQueryType,
            final Matcher methodNameMatcher
    ) {
        return new PrimitiveQueryTypePredicate(lowercaseFirstLetter(methodNameMatcher.group(2)));
    }

    @Override
    protected Optional<Type> extractResultType(final Method method) {
        return extractToPrimitiveMany(method);
    }

    @Override
    protected Optional<Type> extractQueryType(final Method method) {
        return extractFromSinglePrimitive(method);
    }
}
