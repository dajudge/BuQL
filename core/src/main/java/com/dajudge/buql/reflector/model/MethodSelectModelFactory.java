package com.dajudge.buql.reflector.model;

import com.dajudge.buql.reflector.model.translate.ReflectorPredicateVisitor;
import com.dajudge.buql.reflector.model.visitor.QueryTypeWrapper;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodSelectModelFactory {
    public static <Q, R> MethodSelectModel<Q, R> createSelectModel(
            final String tableName,
            final Class<Q> queryType,
            final Class<R> resultType,
            final Function<Class<R>, List<ResultField<R>>> resultFieldAnalyzer,
            final Function<Map<String, List<R>>, ? extends Object> postProcessor
    ) {
        final ReflectorPredicate predicate = new QueryTypeWrapper(queryType, o -> o)
                .visit(new ReflectorPredicateVisitor());
        return createSelectModel(tableName, predicate, resultType, resultFieldAnalyzer, postProcessor);
    }

    public static <Q, R> MethodSelectModel<Q, R> createSelectModel(
            final String tableName,
            final ReflectorPredicate predicate,
            final Class<R> resultType,
            final Function<Class<R>, List<ResultField<R>>> resultFieldAnalyzer,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        final List<ResultField<R>> resultFields = resultFieldAnalyzer.apply(resultType);
        final Supplier<R> factory = () -> factory(resultType);
        return new MethodSelectModel<>(predicate, tableName, resultFields, factory, postProcessor);
    }

    private static <R> R factory(final Class<R> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot instantiate result type instance", e);
        }
    }
}
