package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.api.Analyzer;
import com.dajudge.buql.analyzer.methodmatcher.MethodMatcher;
import com.dajudge.buql.analyzer.methodmatcher.MethodMatcherResult;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.select.MethodSelectModel;
import com.dajudge.buql.reflector.model.select.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;

import static com.dajudge.buql.reflector.model.select.SelectMethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.util.StringUtils.lowercaseFirstLetter;

public abstract class BaseSelectAnalyzer<Q, R> implements Analyzer {
    private final MethodMatcher methodMatcher;

    BaseSelectAnalyzer(final MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        return methodMatcher.match(method).map(result -> createQuery(tableName, result));
    }

    private ReflectSelectQuery<Q, R> createQuery(final String tableName, final MethodMatcherResult methodMatcherResult) {
        final Matcher methodNameMatcher = methodMatcherResult.methodNameMatcher();
        final Class<Q> actualQueryClass = (Class<Q>) methodMatcherResult.getParameterType(0);
        final Class<R> actualResultClass = (Class<R>) methodMatcherResult.getReturnType();
        final String queryFieldName = lowercaseFirstLetter(methodNameMatcher.group(2));
        final ReflectorPredicate predicate = createPredicate(actualQueryClass, queryFieldName);
        final String resultFieldName = lowercaseFirstLetter(methodNameMatcher.group(1));
        final ResultTypeModel<R> resultFieldsModel = createResultFieldsModel(actualResultClass, resultFieldName);
        final MethodSelectModel<Q, R> model = new MethodSelectModel<>(
                predicate,
                tableName,
                resultFieldsModel.getResultFields(),
                resultFieldsModel::newResultInstance,
                createPreProcessor(),
                createPostProcessor()
        );
        return translateMethodModelToQuery(model);
    }

    protected abstract Function<Object, Map<String, Q>> createPreProcessor();

    protected abstract ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final String resultFieldName
    );

    protected abstract Function<Map<String, List<R>>, ?> createPostProcessor();

    protected abstract ReflectorPredicate createPredicate(
            final Class<?> actualQueryClass,
            final String predicateName
    );
}
