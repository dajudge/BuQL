package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.MethodSelectModel;
import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ParameterExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dajudge.buql.analyzer.QueryTypeExtractors.extractFromComplexBulkMap;
import static com.dajudge.buql.analyzer.QueryTypeExtractors.extractFromPrimitiveBulkMap;
import static com.dajudge.buql.analyzer.ResultTypeExtractors.extractToComplexUniqueMap;
import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;
import static java.util.Locale.US;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;

public class PrimitiveBulkToComplexUniqueAnalyzer implements Analyzer {
    private static final Pattern METHOD_NAME_PATTERN = compile("getBy([A-Z].*)");

    @Override
    public Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method) {
        final Optional<Type> queryType = extractFromPrimitiveBulkMap(method);
        final Optional<Type> resultType = extractToComplexUniqueMap(method);
        if (!matchesName(method) || !queryType.isPresent() || !resultType.isPresent()) {
            return Optional.empty();
        }
        final String fieldName = getFieldNameOf(method);
        final ReflectorPredicate predicate = new ReflectorPredicate() {
            @Override
            public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
                return visitor.eq(new DatabaseFieldExpression(fieldName), new ParameterExpression(identity()));
            }
        };
        final MethodSelectModel<?, ?> model = createSelectModel(
                tableName,
                predicate,
                (Class<?>) resultType.get(),
                ComplexResultFieldsAnalyzer::createComplexResultFieldsAnalyzer,
                new UniquePostProcessor<>()
        );
        return Optional.of(translateMethodModelToQuery(model));
    }

    private String getFieldNameOf(final Method method) {
        final Matcher matcher = METHOD_NAME_PATTERN.matcher(method.getName());
        matcher.matches();
        final String fieldName = matcher.group(1);
        return fieldName.substring(0, 1).toLowerCase(US) + fieldName.substring(1);
    }

    private static boolean matchesName(final Method method) {
        return METHOD_NAME_PATTERN.matcher(method.getName()).matches();
    }
}
