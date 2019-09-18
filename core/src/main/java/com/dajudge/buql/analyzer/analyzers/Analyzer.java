package com.dajudge.buql.analyzer.analyzers;

import com.dajudge.buql.reflector.ReflectSelectQuery;

import java.lang.reflect.Method;
import java.util.Optional;

public interface Analyzer {
    Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method);
}
