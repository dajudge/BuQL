package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.reflector.annotations.Transient;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.api.ReflectorQueryTypeTransform;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.EQUALS;
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
        try {
            this.bean = Introspector.getBeanInfo(type);
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not introspect bean " + type.getName(), e);
        }
    }

    public List<ReflectorPredicate> getOperands() {
        return Stream.of(bean.getPropertyDescriptors())
                .filter(it -> it.getReadMethod() != null)
                .filter(it -> it.getReadMethod().getDeclaringClass() != Object.class)
                .filter(it -> it.getReadMethod().getAnnotation(Transient.class) == null)
                .map(this::createPredicate)
                .collect(toList());
    }

    private ReflectorPredicate createPredicate(final PropertyDescriptor prop) {
        return new ComparisonPredicate(
                prop,
                parentAccessor,
                selectOperator(prop),
                selectTransform(prop)
        );
    }

    private Function<Object, Object> selectTransform(final PropertyDescriptor prop) {
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

    private ReflectorCompareOperator selectOperator(final PropertyDescriptor prop) {
        final List<ReflectorOperator> candiates = OPERATORS.stream()
                .filter(it -> it.appliesTo(prop))
                .collect(toList());
        if (candiates.size() > 1) {
            throw new IllegalStateException("Ambiguous predicate: " + prop);
        }
        if (candiates.isEmpty()) {
            return EQUALS;
        }
        return candiates.get(0).getOperator();
    }
}
