package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.reflector.annotations.Nullable;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.api.ReflectorQueryTypeTransform;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.dajudge.buql.reflector.predicate.ReflectorBooleanOperator.AND;
import static com.dajudge.buql.reflector.predicate.ReflectorBooleanOperator.OR;
import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.EQUALS;
import static com.dajudge.buql.util.BeanUtils.getBeanInfo;
import static com.dajudge.buql.util.BeanUtils.getRelevantProperties;
import static java.util.Arrays.asList;
import static java.util.ServiceLoader.load;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class ImplicitTypeReflector {
    private static final Collection<ReflectorOperator> OPERATORS = loadExtensionsOfType(ReflectorOperator.class);
    private static final Collection<ReflectorQueryTypeTransform> TRANSFORMS =
            loadExtensionsOfType(ReflectorQueryTypeTransform.class);

    private static <T> Collection<T> loadExtensionsOfType(final Class<T> type) {
        return stream(load(type).spliterator(), false).collect(toList());
    }

    private final BeanInfo bean;
    private final Function<Object, Object> parentAccessor;

    public ImplicitTypeReflector(
            final Class<?> type,
            final Function<Object, Object> parentAccessor
    ) {
        this.parentAccessor = parentAccessor;
        this.bean = getBeanInfo(type);
    }

    public List<ReflectorPredicate> getOperands() {
        return getRelevantProperties(bean)
                .map(this::predicateFor)
                .collect(toList());
    }

    private ReflectorPredicate predicateFor(final PropertyDescriptor prop) {
        final DatabaseFieldExpression dbField = databaseFieldExpressionFor(prop);
        final ParameterExpression param = parameterExpressionFor(prop);
        final ComparisonPredicate propPredicate = new ComparisonPredicate(operatorFor(prop), dbField, param);
        if (isNullable(prop)) {
            final ReflectorPredicate left = new IsNullPredicate(dbField);
            final ReflectorPredicate right = new IsNullPredicate(param);
            final ReflectorPredicate nullablePredicate = new BooleanOperationPredicate(AND, asList(left, right));
            return new BooleanOperationPredicate(OR, asList(propPredicate, nullablePredicate));
        }
        return propPredicate;
    }

    private boolean isNullable(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(Nullable.class) != null;
    }

    private DatabaseFieldExpression databaseFieldExpressionFor(final PropertyDescriptor prop) {
        return new DatabaseFieldExpression(prop);
    }

    private ParameterExpression parameterExpressionFor(final PropertyDescriptor prop) {
        return new ParameterExpression(getterFor(prop).andThen(parameterTransformFor(prop)));
    }

    private Function<Object, Object> getterFor(final PropertyDescriptor prop) {
        return o -> {
            try {
                return prop.getReadMethod().invoke(parentAccessor.apply(o));
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to read filter value from " + prop, e);
            }
        };
    }

    private Function<Object, Object> parameterTransformFor(final PropertyDescriptor prop) {
        final List<ReflectorQueryTypeTransform> candiates = TRANSFORMS.stream()
                .filter(it -> it.appliesTo(prop))
                .collect(toList());
        if (candiates.size() > 1) {
            throw new IllegalStateException("Ambiguous query type transform: " + prop);
        }
        if (candiates.isEmpty()) {
            return identity();
        }
        return candiates.get(0)::apply;
    }

    private ReflectorCompareOperator operatorFor(final PropertyDescriptor prop) {
        final List<ReflectorOperator> candidates = OPERATORS.stream()
                .filter(it -> it.appliesTo(prop))
                .collect(toList());
        if (candidates.size() > 1) {
            throw new IllegalStateException("Ambiguous predicate: " + prop);
        }
        if (candidates.isEmpty()) {
            return EQUALS;
        }
        return candidates.get(0).getOperator();
    }
}
