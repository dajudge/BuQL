package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.ReflectionUtil;
import com.dajudge.buql.reflector.model.MethodSelectModelFactory.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SyncParamToReturnValueSelectAnalyzer<Q, R> extends BaseSelectAnalyzer<Q, R> {
    private final Function<Map<String, ?>, ?> queryMultiplicityResultTransform;
    private final Function<Map<String, List<R>>, Map<String, ?>> resultMultiplicityTransform;
    private final ResultTypeToPredicateFactory resultFieldsModelFactory;
    private final QueryPreprocessorFactory queryPreprocessorFactory;
    private final ReflectorPredicateFactory predicateFactory;
    private final Predicate<Type> resultTypeComplexityPredicate;
    private final Function<Predicate<Type>, Predicate<Type>> resultTypeMultiplicityTransform;
    private final Function<Predicate<Type>, Predicate<Type>> queryTypeMultiplicityTransform;
    private final Predicate<Type> queryTypeComplexityPredicate;

    private static Pattern createPattern(
            final QueryTypeComplexity queryTypeComplexity,
            final ResultMultiplicity resultMultiplicity,
            final ResultTypeComplexity resultTypeComplexity
    ) {
        final String patternString = resultMultiplicity.getPatternPrefix()
                + "("
                + resultTypeComplexity.getResultTypePattern()
                + ")By("
                + queryTypeComplexity.getQueryTypePattern()
                + ")";
        return Pattern.compile(patternString);
    }

    public SyncParamToReturnValueSelectAnalyzer(
            final QueryMultiplicity queryMultiplicity,
            final QueryTypeComplexity queryTypeComplexity,
            final ResultMultiplicity resultMultiplicity,
            final ResultTypeComplexity resultTypeComplexity
    ) {
        super(createPattern(
                queryTypeComplexity,
                resultMultiplicity,
                resultTypeComplexity
        ));
        this.queryMultiplicityResultTransform = queryMultiplicity.getPostProcessor();
        this.resultMultiplicityTransform = resultMultiplicity.getPostProcessor();
        this.resultFieldsModelFactory = resultTypeComplexity.getPredicateFactory();
        this.queryPreprocessorFactory = queryMultiplicity.getPreProcessorFactory();
        this.predicateFactory = queryTypeComplexity.getPredicateFactory();
        this.resultTypeComplexityPredicate = resultTypeComplexity.getPredicate();
        this.resultTypeMultiplicityTransform = resultMultiplicity.getTransform();
        this.queryTypeMultiplicityTransform = queryMultiplicity.getTransform();
        this.queryTypeComplexityPredicate = queryTypeComplexity.getPredicate();
    }

    @Override
    protected final Function<Map<String, List<R>>, ?> createPostProcessor() {
        return resultMultiplicityTransform.andThen(queryMultiplicityResultTransform);
    }

    @Override
    protected final ResultTypeModel<R> createResultFieldsModel(final Class<R> clazz, final String resultFieldName) {
        return resultFieldsModelFactory.createResultFieldsModel(clazz, resultFieldName);
    }

    @Override
    protected final Function<Object, Map<String, Q>> createPreProcessor() {
        return queryPreprocessorFactory.createPreProcessor();
    }

    @Override
    protected final ReflectorPredicate createPredicate(final Class<?> actualQueryClass, final String predicateName) {
        return predicateFactory.createPredicate(actualQueryClass, predicateName);
    }

    @Override
    protected final Optional<Type> extractResultType(final Type resultType) {
        final ReflectionUtil.TypeCaptor captor = new ReflectionUtil.TypeCaptor();
        final Predicate<Type> predicate = resultTypeMultiplicityTransform
                .apply(captor.captureIf(resultTypeComplexityPredicate));
        queryTypeMultiplicityTransform.apply(predicate).test(resultType);
        return captor.getCapturedType();
    }

    @Override
    protected final Optional<Type> extractQueryType(final Type queryType) {
        return Optional.of(queryType).map(t -> {
            final ReflectionUtil.TypeCaptor captor = new ReflectionUtil.TypeCaptor();
            queryTypeMultiplicityTransform.apply(captor.captureIf(queryTypeComplexityPredicate)).test(t);
            return captor.getCapturedType().orElse(null);
        });
    }

}
