package com.dajudge.buql;

import com.dajudge.buql.analyzer.analyzers.*;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.util.CollectorCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedMap;
import static java.util.stream.Collectors.toList;

public class BuQL {
    private static final Collection<Analyzer> CONVERTERS = asList(
            new BulkComplexToManyComplexSelectAnalyzer(),
            new BulkComplexToUniqueComplexSelectAnalyzer(),
            new BulkPrimitiveToUniqueComplexSelectAnalyzer(),
            new BulkPrimitiveToUniquePrimitiveSelectAnalyzer(),
            new BulkComplexToManyPrimitiveSelectAnalyzer(),
            new SinglePrimitiveToUniquePrimitiveSelectAnalyzer(),
            new SinglePrimitiveToUniqueComplexSelectAnalyzer(),
            new SinglePrimitiveToManyComplexSelectAnalyzer(),
            new SinglePrimitiveToManyPrimitiveSelectAnalyzer(),
            new SingleComplexToUniquePrimitiveSelectAnalyzer(),
            new SingleComplexToUniqueComplexSelectAnalyzer(),
            new SingleComplexToManyPrimitiveSelectAnalyzer(),
            new SingleComplexToManyComplexSelectAnalyzer()
    );
    private final Dialect dialect;
    private final DatabaseEngine engine;
    private final Map<Method, ReflectSelectQuery<?, ?>> cachedQueries = synchronizedMap(new HashMap<>());

    public BuQL(final Dialect dialect, final DatabaseEngine engine) {
        assert dialect != null;
        assert engine != null;
        this.dialect = dialect;
        this.engine = engine;
    }

    public <T> T up(final Class<T> iface) {
        final Table tableAnnotation = iface.getAnnotation(Table.class);
        final String tableName = tableAnnotation.value();
        final Function<Method, ReflectSelectQuery<?, ?>> compiler = m -> compile(tableName, m);
        final Object proxy = newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{iface},
                createInvocationHandler(compiler)
        );
        return (T) proxy;
    }

    private InvocationHandler createInvocationHandler(
            final Function<Method, ReflectSelectQuery<?, ?>> compilerFactory
    ) {
        return (o, method, objects) -> invocationHandler(compilerFactory::apply, method, objects);
    }

    private <Q, R> Object invocationHandler(
            final Function<Method, ReflectSelectQuery<Q, R>> compilerFactory,
            final Method method,
            final Object[] objects
    ) {
        final ReflectSelectQuery<Q, R> query = getCachedOrNewQuery(compilerFactory, method);
        final Map<String, Q> params = (Map<String, Q>) query.preProcess(objects[0]);
        final CollectorCallback<R> cb = new CollectorCallback<>(params.keySet());
        query.execute(dialect, engine, params, cb);
        return query.postProcess(cb.getResult());
    }

    private <Q, R> ReflectSelectQuery<Q, R> getCachedOrNewQuery(
            final Function<Method, ReflectSelectQuery<Q, R>> compiler,
            final Method method
    ) {
        final ReflectSelectQuery<?, ?> query = cachedQueries.computeIfAbsent(method, compiler);
        return (ReflectSelectQuery<Q, R>) query;
    }

    private ReflectSelectQuery<?, ?> compile(final String tableName, final Method method) {
        final List<? extends ReflectSelectQuery<?, ?>> candidates = CONVERTERS.stream()
                .map(c -> c.convert(tableName, method))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        if (candidates.size() > 1) {
            throw new IllegalArgumentException("Ambiguous compilation strategies for " + method);
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Don't know how to compile " + method);
        }
        return candidates.get(0);
    }

}
