package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.ReflectSelectQuery;

import java.lang.reflect.Method;
import java.util.Optional;

public interface Analyzer {
    Optional<ReflectSelectQuery<?, ?>> convert(final String tableName, final Method method);
}
