package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.Analyzer;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.select.MethodSelectModel;
import com.dajudge.buql.reflector.model.select.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dajudge.buql.reflector.model.select.SelectMethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.util.StringUtils.lowercaseFirstLetter;

public abstract class BaseSelectAnalyzer<Q, R> implements Analyzer {
    private final Pattern methodNamePattern;

    BaseSelectAnalyzer(final Pattern methodNamePattern) {
        this.methodNamePattern = methodNamePattern;
    }

    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        final Optional<Type> queryType = extractQueryType(method.getGenericParameterTypes()[0]);
        final Optional<Type> resultType = extractResultType(method.getGenericReturnType());
        final Matcher methodNameMatcher = matchMethodName(method);
        if (!appliesTo(queryType, resultType, methodNameMatcher)) {
            return Optional.empty();
        }
        final Class<Q> actualQueryClass = (Class<Q>) queryType.get();
        final Class<R> actualResultClass = (Class<R>) resultType.get();
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
        return Optional.of(translateMethodModelToQuery(model));
    }

    public boolean appliesTo(final Optional<Type> queryType, final Optional<Type> resultType, final Matcher methodNameMatcher) {
        final boolean methodNameMatches = methodNameMatcher.matches();
        final boolean queryTypeMatches = queryType.isPresent();
        final boolean resultTypeMatches = resultType.isPresent();
        return methodNameMatches && queryTypeMatches && resultTypeMatches;
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

    protected abstract Optional<Type> extractResultType(final Type resultType);

    protected abstract Optional<Type> extractQueryType(final Type queryType);

    private Matcher matchMethodName(final Method method) {
        return methodNamePattern.matcher(method.getName());
    }

}
