package com.dajudge.buql;

import com.dajudge.buql.analyzer.Analyzer;
import com.dajudge.buql.analyzer.BulkToManyAnalyzer;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.ReflectSelectQuery.Callback;
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
            new BulkToManyAnalyzer()
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
            final Function<Method, ReflectSelectQuery<?, ?>> compiler
    ) {
        return (o, method, objects) -> invocationHandler(compiler::apply, method, objects);
    }

    private <Q, R> Map<String, List<R>> invocationHandler(
            final Function<Method, ReflectSelectQuery<Q, R>> compiler,
            final Method method,
            final Object[] objects
    ) {
        final ReflectSelectQuery<Q, R> query = getCachedOrNewQuery(compiler, method);
        final Map<String, Q> params = (Map<String, Q>) objects[0];
        final CollectorCallback<R> cb = new CollectorCallback<>(params.keySet());
        query.execute(dialect, engine, params, cb);
        return cb.getResult();
    }

    private <Q, R> ReflectSelectQuery<Q, R> getCachedOrNewQuery(
            final Function<Method, ReflectSelectQuery<Q, R>> compiler,
            final Method method
    ) {
        final ReflectSelectQuery<?, ?> query = cachedQueries.computeIfAbsent(method, compiler);
        return (ReflectSelectQuery<Q, R>) query;
    }

    private ReflectSelectQuery<?, ?> compile(final String tableName, final Method method) {
        final List<Optional<ReflectSelectQuery<?, ?>>> candidates = CONVERTERS.stream()
                .map(c -> c.convert(tableName, method))
                .filter(Optional::isPresent)
                .collect(toList());
        if (candidates.size() > 1) {
            throw new IllegalArgumentException("Ambiguous compilation strategies for " + method);
        }
        return candidates.get(0).orElseThrow(() ->
                new IllegalArgumentException("Don't know how to compile " + method)
        );
    }

}
