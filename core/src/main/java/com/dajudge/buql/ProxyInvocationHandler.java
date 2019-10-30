package com.dajudge.buql;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectDatabaseOperation;
import com.dajudge.buql.reflector.ReflectQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.synchronizedMap;

class ProxyInvocationHandler<Q, R, I> implements InvocationHandler {
    private final Map<Method, ReflectDatabaseOperation<Q, I, R>> cachedQueries = synchronizedMap(new HashMap<>());
    private final Dialect dialect;
    private final DatabaseEngine engine;
    private final Function<Method, ReflectDatabaseOperation<Q, I, R>> compilerFactory;

    ProxyInvocationHandler(
            final Dialect dialect,
            final DatabaseEngine engine,
            final Function<Method, ReflectDatabaseOperation<Q, I, R>> compilerFactory
    ) {
        this.dialect = dialect;
        this.engine = engine;
        this.compilerFactory = compilerFactory;
    }

    @Override
    public Object invoke(final Object o, final Method method, final Object[] objects) {
        final ReflectDatabaseOperation<Q, I, R> query = getCachedOrNewQuery(compilerFactory, method);
        final I params = query.preProcess(objects[0]);
        final ReflectQuery.Callback<R> cb = query.createCallback(params);
        query.execute(dialect, engine, params, cb);
        return query.postProcess(cb.getResult());
    }

    private  ReflectDatabaseOperation<Q, I, R> getCachedOrNewQuery(
            final Function<Method, ReflectDatabaseOperation<Q, I, R>> compiler,
            final Method method
    ) {
        return cachedQueries.computeIfAbsent(method, compiler);
    }

}
