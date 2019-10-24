package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.ReflectDatabaseOperation;

import java.lang.reflect.Method;
import java.util.Optional;

public interface Analyzer {
    Optional<? extends ReflectDatabaseOperation<?, ?, ?>> convert(final String tableName, final Method method);
}
