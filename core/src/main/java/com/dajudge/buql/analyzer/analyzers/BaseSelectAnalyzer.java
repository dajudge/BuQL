package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.MethodSelectModel;
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

import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;

public abstract class BaseSelectAnalyzer implements Analyzer {
private final Pattern methodNamePattern;

    protected BaseSelectAnalyzer(final Pattern methodNamePattern) {
        this.methodNamePattern = methodNamePattern;
    }

    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        final Optional<Type> queryType = extractQueryType(method);
        final Optional<Type> resultType = extractResultType(method);
        final Matcher methodNameMatcher = matchMethodName(method);
        if (!methodNameMatcher.matches() || !queryType.isPresent() || !resultType.isPresent()) {
            return Optional.empty();
        }
        final Class<?> actualQueryClass = (Class<?>) queryType.get();
        final Class<?> actualResultClass = (Class<?>) resultType.get();
        final ReflectorPredicate predicate = createPredicate(method, actualQueryClass, methodNameMatcher);
        final MethodSelectModel<?, ?> model = createSelectModel(
                tableName,
                predicate,
                createResultFieldsModel(actualResultClass, methodNameMatcher),
                createPostProcessor()
        );
        return Optional.of(translateMethodModelToQuery(model));
    }

    protected abstract <R> ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final Matcher methodNameMatcher
    );

    protected abstract <T> Function<Map<String, List<T>>, ?> createPostProcessor();

    protected abstract ReflectorPredicate createPredicate(
            final Method method,
            final Class<?> actualQueryClass,
            final Matcher methodNameMatcher
    );

    protected abstract Optional<Type> extractResultType(final Method method);

    protected abstract Optional<Type> extractQueryType(final Method method);

    private Matcher matchMethodName(final Method method) {
        return methodNamePattern.matcher(method.getName());
    }

}
