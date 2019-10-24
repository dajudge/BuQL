package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.Analyzer;
import com.dajudge.buql.analyzer.ReflectionUtil;
import com.dajudge.buql.reflector.ReflectInsertQuery;
import com.dajudge.buql.reflector.model.insert.MethodInsertModel;
import com.dajudge.buql.reflector.predicate.DatabaseFieldExpression;
import com.dajudge.buql.reflector.predicate.ReflectorExpressionVisitor;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;
import static com.dajudge.buql.analyzer.ReflectionUtil.listOf;
import static com.dajudge.buql.reflector.model.insert.InsertMethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.util.BeanUtils.getBeanInfo;
import static com.dajudge.buql.util.BeanUtils.getRelevantProperties;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class InsertCollectionAnalyzer implements Analyzer {
    @Override
    public Optional<ReflectInsertQuery<?>> convert(final String tableName, final Method method) {
        if (!method.getName().equals("create")) {
            return Optional.empty();
        }
        if (method.getGenericParameterTypes().length != 1) {
            return Optional.empty();
        }
        final ReflectionUtil.TypeCaptor captor = new ReflectionUtil.TypeCaptor();
        final boolean hasSuitableParam = listOf(captor.captureIf(isComplexType()))
                .test(method.getGenericParameterTypes()[0]);
        if (!hasSuitableParam) {
            return Optional.empty();
        }
        final BeanInfo beanInfo = getBeanInfo((Class<?>) captor.getCapturedType().get());
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
                .collect(Collectors.toList());
        final BiFunction<?, String, Object> valueAccessor = (o, f) -> {
            try {
                return relevantProperties.get(f).getReadMethod().invoke(o);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke getter.", e);
            }
        };
        final MethodInsertModel<?> methodModel = new MethodInsertModel<>(tableName, insertFields, valueAccessor);
        return Optional.of(translateMethodModelToQuery(methodModel));
    }
}
