package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.ComplexResultFieldsAnalyzer;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.MethodSelectModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;

public abstract class BaseSelectAnalyzer implements Analyzer {

    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        final Optional<Type> queryType = extractQueryType(method);
        final Optional<Type> resultType = extractResultType(method);
        if (!matchesName(method) || !queryType.isPresent() || !resultType.isPresent()) {
            return Optional.empty();
        }
        final Class<?> actualQueryClass = (Class<?>) queryType.get();
        final ReflectorPredicate predicate = toPredicate(method, actualQueryClass);
        final MethodSelectModel<?, ?> model = createSelectModel(
                tableName,
                predicate,
                (Class<?>) resultType.get(),
                ComplexResultFieldsAnalyzer::createComplexResultFieldsAnalyzer,
                getPostProcessor()
        );
        return Optional.of(translateMethodModelToQuery(model));
    }

    protected abstract <T> Function<Map<String, List<T>>, ?> getPostProcessor();

    protected abstract ReflectorPredicate toPredicate(final Method method, final Class<?> actualQueryClass);

    protected abstract Optional<Type> extractResultType(final Method method);

    protected abstract Optional<Type> extractQueryType(final Method method);

    protected abstract boolean matchesName(final Method method);
}
