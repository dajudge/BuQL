package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.MethodSelectModel;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dajudge.buql.analyzer.QueryTypeExtractors.extractFromComplexBulkMap;
import static com.dajudge.buql.analyzer.ResultTypeExtractors.extractToComplexUniqueMap;
import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;
import static java.util.stream.Collectors.toMap;

public class ComplexBulkToComplexUniqueAnalyzer implements Analyzer {
    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        final Optional<Type> queryType = extractFromComplexBulkMap(method);
        final Optional<Type> resultType = extractToComplexUniqueMap(method);
        if (!matchesName(method) || !queryType.isPresent() || !resultType.isPresent()) {
            return Optional.empty();
        }
        final MethodSelectModel<?, ?> model = createSelectModel(
                tableName,
                (Class<?>) queryType.get(),
                (Class<?>) resultType.get(),
                ComplexResultFieldsAnalyzer::createComplexResultFieldsAnalyzer,
                new UniquePostProcessor<>()
        );
        return Optional.of(translateMethodModelToQuery(model));
    }

    private static boolean matchesName(final Method method) {
        return method.getName().matches("getBy[A-Z].*");
    }
}
