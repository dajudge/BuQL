package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.ReflectionUtil;
import com.dajudge.buql.analyzer.api.Analyzer;
import com.dajudge.buql.analyzer.methodmatcher.MethodMatcher;
import com.dajudge.buql.analyzer.methodmatcher.MethodMatcherBuilder;
import com.dajudge.buql.analyzer.methodmatcher.MethodMatcherResult;
import com.dajudge.buql.reflector.ReflectInsertQuery;
import com.dajudge.buql.reflector.model.insert.MethodInsertModel;
import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ReflectorExpressionVisitor;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.dajudge.buql.reflector.model.insert.InsertMethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.util.BeanUtils.getBeanInfo;
import static com.dajudge.buql.util.BeanUtils.getRelevantProperties;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

abstract class BaseInsertAnalyzer implements Analyzer {
    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("^create[From[A-Z].*]?$");
    private final Function<Predicate<Type>, Predicate<Type>> multiplicityTransform;
    private final Function<ReflectionUtil.TypeCaptor, Predicate<Type>> typePredicate;
    private final MethodMatcher matcher;

    protected BaseInsertAnalyzer(
            final Function<Predicate<Type>, Predicate<Type>> multiplicityTransform,
            final Function<ReflectionUtil.TypeCaptor, Predicate<Type>> typePredicate
    ) {
        this.multiplicityTransform = multiplicityTransform;
        this.typePredicate = typePredicate;
        matcher = MethodMatcherBuilder.newMatcher(METHOD_NAME_PATTERN)
                .requiresParam(multiplicityTransform, typePredicate)
                .build();
    }

    @Override
    public Optional<ReflectInsertQuery<?>> convert(final String tableName, final Method method) {
        return matcher.match(method).map(methodMatcherResult -> createQuery(tableName, methodMatcherResult));
    }

    private ReflectInsertQuery<?> createQuery(final String tableName, final MethodMatcherResult matcherResult) {
        final BeanInfo beanInfo = getBeanInfo((Class<?>) matcherResult.getParameterType(0));
        final Map<String, PropertyDescriptor> relevantProperties = getRelevantProperties(beanInfo)
                .collect(toMap(FeatureDescriptor::getName, identity()));
        final ReflectorExpressionVisitor<String> visitor = new ReflectorExpressionVisitor<String>() {
            @Override
            public String parameter(final Function<Object, Object> read) {
                return null;
            }

            @Override
            public String databaseField(final String colName) {
                return colName;
            }
        };
        final Collection<String> insertFields = relevantProperties.values().stream()
                .map(DatabaseFieldExpression::new)
                .map(it -> it.visit(visitor))
                .collect(toList());
        final BiFunction<?, String, Object> valueAccessor = (o, f) -> {
            try {
                return relevantProperties.get(f).getReadMethod().invoke(o);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke getter.", e);
            }
        };
        final MethodInsertModel<?> methodModel = new MethodInsertModel<>(
                tableName,
                insertFields,
                valueAccessor,
                createPreProcessor()
        );
        return translateMethodModelToQuery(methodModel);
    }

    protected abstract <Q> Function<Object, List<Q>> createPreProcessor();
}
