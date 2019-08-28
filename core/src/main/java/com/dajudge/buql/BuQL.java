package com.dajudge.buql;

import com.dajudge.buql.analyzer.Analyzer;
import com.dajudge.buql.analyzer.BulkToManyAnalyzer;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.util.CollectorCallback;

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
        final Object proxy = newProxyInstance(getClass().getClassLoader(), new Class[]{iface}, (o, method, objects) -> {
            final ReflectSelectQuery<?, ?> query = cachedQueries.computeIfAbsent(method, compiler);
            final CollectorCallback cb = new CollectorCallback();
            final Map params = (Map<String, ?>) objects[0];
            query.execute(dialect, engine, params, cb);
            return cb.getResult();
        });
        return (T) proxy;
    }

    private ReflectSelectQuery<?, ?> compile(final String tableName, final Method method) {
        final List<Optional<ReflectSelectQuery<?, ?>>> candidates = CONVERTERS.stream()
                .map(c -> c.convert(tableName, method))
                .filter(c -> c.isPresent())
                .collect(toList());
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Don't know how to compile " + method);
        } else if (candidates.size() > 1) {
            throw new IllegalArgumentException("Ambiguous compilation strategies for " + method);
        }
        return candidates.get(0).get();
    }

}
