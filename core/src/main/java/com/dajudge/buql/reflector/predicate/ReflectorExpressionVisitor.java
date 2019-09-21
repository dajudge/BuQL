package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public interface ReflectorExpressionVisitor<T> {
    T parameter(Function<Object, Object> read);

    T databaseField(String colName);
}
