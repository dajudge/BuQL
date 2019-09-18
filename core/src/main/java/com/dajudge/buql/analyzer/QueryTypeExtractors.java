package com.dajudge.buql.analyzer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.ReflectionUtil.*;

final class QueryTypeExtractors {
    private QueryTypeExtractors() {
    }

    static Optional<Type> extractFromComplexBulkMap(final Method method) {
        return extractFromBulkMap(method, isComplexType());
    }

    static Optional<Type> extractFromPrimitiveBulkMap(final Method method) {
        return extractFromBulkMap(method, isPrimitiveType());
    }

    private static Optional<Type> extractFromBulkMap(final Method method, final Predicate<Type> queryTypePredicate) {
        final Type[] types = method.getGenericParameterTypes();
        if (types.length != 1) {
            return Optional.empty();
        }
        final TypeCaptor captor = new TypeCaptor();
        mapOf(assignableTo(String.class), captor.captureIf(queryTypePredicate)).test(types[0]);
        return captor.getCapturedType();
    }
}
