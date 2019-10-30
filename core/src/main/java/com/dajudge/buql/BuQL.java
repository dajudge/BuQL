package com.dajudge.buql;

import com.dajudge.buql.analyzer.api.Analyzer;
import com.dajudge.buql.analyzer.api.AnalyzerFactory;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectDatabaseOperation;
import com.dajudge.buql.reflector.annotations.Table;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class BuQL {
    private static final Collection<Analyzer> ANALYZERS = collectAnalyzers();

    private final Dialect dialect;
    private final DatabaseEngine engine;

    public BuQL(final Dialect dialect, final DatabaseEngine engine) {
        assert dialect != null;
        assert engine != null;
        this.dialect = dialect;
        this.engine = engine;
    }

    @SuppressWarnings("unchecked")
    public <T> T up(final Class<T> iface) {
        final Table tableAnnotation = iface.getAnnotation(Table.class);
        final String tableName = tableAnnotation.value();
        final Object proxy = newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{iface},
                new ProxyInvocationHandler<>(dialect, engine, m -> compile(tableName, m))
        );
        return (T) proxy;
    }

    @SuppressWarnings("unchecked")
    private <Q,R,I> ReflectDatabaseOperation<Q, I, R> compile(final String tableName, final Method method) {
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
        return (ReflectDatabaseOperation<Q, I, R>) candidates.get(0);
    }

    private static Collection<Analyzer> collectAnalyzers() {
        return StreamSupport.stream(ServiceLoader.load(AnalyzerFactory.class).spliterator(), false)
                .map(AnalyzerFactory::create)
                .flatMap(identity())
                .collect(Collectors.toList());
    }

}
