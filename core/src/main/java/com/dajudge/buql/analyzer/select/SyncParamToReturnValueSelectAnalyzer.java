package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.methodmatcher.MethodMatcher;
import com.dajudge.buql.reflector.model.select.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.dajudge.buql.analyzer.methodmatcher.MethodMatcherBuilder.newMatcher;

public class SyncParamToReturnValueSelectAnalyzer<Q, R> extends BaseSelectAnalyzer<Q, R> {
    private final Function<Map<String, ?>, ?> queryMultiplicityResultTransform;
    private final Function<Map<String, List<R>>, Map<String, ?>> resultMultiplicityTransform;
    private final ResultTypeToPredicateFactory resultFieldsModelFactory;
    private final QueryPreprocessorFactory queryPreprocessorFactory;
    private final ReflectorPredicateFactory predicateFactory;

    public SyncParamToReturnValueSelectAnalyzer(
            final QueryMultiplicity queryMultiplicity,
            final QueryTypeComplexity queryTypeComplexity,
            final ResultMultiplicity resultMultiplicity,
            final ResultTypeComplexity resultTypeComplexity
    ) {
        super(buildInspector(
                queryMultiplicity, queryTypeComplexity, resultMultiplicity, resultTypeComplexity
        ));
        this.queryMultiplicityResultTransform = queryMultiplicity.getPostProcessor();
        this.resultMultiplicityTransform = resultMultiplicity.getPostProcessor();
        this.resultFieldsModelFactory = resultTypeComplexity.getPredicateFactory();
        this.queryPreprocessorFactory = queryMultiplicity.getPreProcessorFactory();
        this.predicateFactory = queryTypeComplexity.getPredicateFactory();
    }

    private static MethodMatcher buildInspector(
            final QueryMultiplicity queryMultiplicity,
            final QueryTypeComplexity queryTypeComplexity,
            final ResultMultiplicity resultMultiplicity,
            final ResultTypeComplexity resultTypeComplexity
    ) {
        return newMatcher(createPattern(queryTypeComplexity, resultMultiplicity, resultTypeComplexity))
                .requiresParam(
                        queryMultiplicity.getTransform(),
                        c -> c.captureIf(queryTypeComplexity.getPredicate())
                )
                .requiresReturnType(
                        queryMultiplicity.getTransform(),
                        c -> resultMultiplicity.getTransform().apply(c.captureIf(resultTypeComplexity.getPredicate()))
                )
                .build();
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
        return Pattern.compile("^" + patternString + "$");
    }
}
