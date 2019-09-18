package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.analyzer.UniquePostProcessor;
import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ParameterExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dajudge.buql.analyzer.typeextractors.QueryTypeExtractors.extractFromPrimitiveBulkMap;
import static com.dajudge.buql.analyzer.typeextractors.ResultTypeExtractors.extractToComplexUniqueMap;
import static java.util.Locale.US;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;

public class PrimitiveBulkToComplexUniqueSelectAnalyzer extends BaseSelectAnalyzer {
    private static final Pattern METHOD_NAME_PATTERN = compile("getBy([A-Z].*)");

    @Override
    protected <T> Function<Map<String, List<T>>, ?> getPostProcessor() {
        return new UniquePostProcessor<>();
    }

    @Override
    protected ReflectorPredicate toPredicate(final Method method, final Class<?> actualQueryType) {
        final String fieldName = getFieldNameOf(method);
        return new ReflectorPredicate() {
            @Override
            public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
                return visitor.eq(new DatabaseFieldExpression(fieldName), new ParameterExpression(identity()));
            }
        };
    }

    @Override
    protected Optional<Type> extractResultType(final Method method) {
        return extractToComplexUniqueMap(method);
    }

    @Override
    protected Optional<Type> extractQueryType(final Method method) {
        return extractFromPrimitiveBulkMap(method);
    }

    @Override
    protected boolean matchesName(final Method method) {
        return METHOD_NAME_PATTERN.matcher(method.getName()).matches();
    }

    private String getFieldNameOf(final Method method) {
        final Matcher matcher = METHOD_NAME_PATTERN.matcher(method.getName());
        matcher.matches();
        final String fieldName = matcher.group(1);
        return fieldName.substring(0, 1).toLowerCase(US) + fieldName.substring(1);
    }
}
