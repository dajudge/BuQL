package com.dajudge.buql;

import com.dajudge.buql.analyzer.Analyzer;
import com.dajudge.buql.analyzer.insert.InsertCollectionAnalyzer;
import com.dajudge.buql.analyzer.select.*;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectDatabaseOperation;
import com.dajudge.buql.reflector.ReflectQuery;
import com.dajudge.buql.reflector.annotations.Table;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Collections.synchronizedMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class BuQL {
    private static final Stream<Analyzer> SELECT_ANALYZERS = Stream.of(QueryMultiplicity.values()).map(queryMultiplicity ->
            Stream.of(QueryTypeComplexity.values()).map(queryTypeComplexity ->
                    Stream.of(ResultMultiplicity.values()).map(resultMultiplicity ->
                            Stream.of(ResultTypeComplexity.values()).map(resultTypeComplexity ->
                                    new SyncParamToReturnValueSelectAnalyzer<>(
                                            queryMultiplicity,
                                            queryTypeComplexity,
                                            resultMultiplicity,
                                            resultTypeComplexity
                                    )))
                            .flatMap(identity()))
                    .flatMap(identity()))
            .flatMap(identity());
    private static final Stream<Analyzer> INSERT_ANALYZERS = Stream.of(
            new InsertCollectionAnalyzer()
    );
    private static final Collection<Analyzer> ANALYZERS = Stream.of(
            SELECT_ANALYZERS,
            INSERT_ANALYZERS
    ).reduce(Stream::concat).get().collect(Collectors.toList());

    private final Dialect dialect;
    private final DatabaseEngine engine;
    private final Map<Method, ReflectDatabaseOperation<?, ?, ?>> cachedQueries = synchronizedMap(new HashMap<>());

    public BuQL(final Dialect dialect, final DatabaseEngine engine) {
        assert dialect != null;
        assert engine != null;
        this.dialect = dialect;
        this.engine = engine;
    }

    public <T> T up(final Class<T> iface) {
        final Table tableAnnotation = iface.getAnnotation(Table.class);
        final String tableName = tableAnnotation.value();
        final Function<Method, ReflectDatabaseOperation<?, ?, ?>> compiler = m -> compile(tableName, m);
        final Object proxy = newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{iface},
                createInvocationHandler(compiler)
        );
        return (T) proxy;
    }

    private InvocationHandler createInvocationHandler(
            final Function<Method, ReflectDatabaseOperation<?, ?, ?>> compilerFactory
    ) {
        return (o, method, objects) -> invocationHandler(compilerFactory::apply, method, objects);
    }

    private <Q, I, R> Object invocationHandler(
            final Function<Method, ReflectDatabaseOperation<Q, I, R>> compilerFactory,
            final Method method,
            final Object[] objects
    ) {
        final ReflectDatabaseOperation<Q, I, R> query = getCachedOrNewQuery(compilerFactory, method);
        final I params = query.preProcess(objects[0]);
        final ReflectQuery.Callback<R> cb = query.createCallback(params);
        query.execute(dialect, engine, params, cb);
        return query.postProcess(cb.getResult());
    }

    private <Q, I, R> ReflectDatabaseOperation<Q, I, R> getCachedOrNewQuery(
            final Function<Method, ReflectDatabaseOperation<Q, I, R>> compiler,
            final Method method
    ) {
        return (ReflectDatabaseOperation<Q, I, R>) cachedQueries.computeIfAbsent(method, compiler);
    }

    private ReflectDatabaseOperation<?, ?, ?> compile(final String tableName, final Method method) {
        final List<? extends ReflectDatabaseOperation<?, ?, ?>> candidates = ANALYZERS.stream()
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
