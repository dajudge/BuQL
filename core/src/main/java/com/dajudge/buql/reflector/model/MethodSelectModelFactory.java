package com.dajudge.buql.reflector.model;

import com.dajudge.buql.reflector.annotations.Transient;
import com.dajudge.buql.reflector.model.translate.ReflectorPredicateVisitor;
import com.dajudge.buql.reflector.model.visitor.QueryTypeWrapper;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MethodSelectModelFactory {
    public static <Q, R> MethodSelectModel<Q, R> createSelectModel(
            final String tableName,
            final Class<Q> queryType,
            final Class<R> resultType
    ) {
        final ReflectorPredicate predicate = new QueryTypeWrapper(queryType, o -> o)
                .visit(new ReflectorPredicateVisitor());
        final List<ResultField<R>> resultFields = getResultFields(resultType);
        final Supplier<R> factory = () -> factory(resultType);
        return new MethodSelectModel<>(predicate, tableName, resultFields, factory);
    }

    private static <R> List<ResultField<R>> getResultFields(final Class<R> resultType) {
        try {
            final BeanInfo resultBean = Introspector.getBeanInfo(resultType);
            return Stream.of(resultBean.getPropertyDescriptors())
                    .filter(prop -> prop.getWriteMethod() != null)
                    .filter(prop -> prop.getWriteMethod().getAnnotation(Transient.class) == null)
                    .map(MethodSelectModelFactory::<R>toResultField)
                    .collect(toList());
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Failed to introspect result type " + resultType.getName(), e);
        }
    }

    private static <R> ResultField<R> toResultField(final PropertyDescriptor prop) {
        return new ResultField<>(prop.getName(), (o, v) -> {
            try {
                prop.getWriteMethod().invoke(o, v);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke setter " + prop.getWriteMethod(), e);
            }
        });
    }

    private static <R> R factory(final Class<R> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot instantiate result type instance", e);
        }
    }
}
