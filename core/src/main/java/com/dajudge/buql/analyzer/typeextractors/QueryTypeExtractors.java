package com.dajudge.buql.analyzer.typeextractors;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.typeextractors.ReflectionUtil.*;

public final class QueryTypeExtractors {
    private QueryTypeExtractors() {
    }

    public static Optional<Type> extractFromComplexBulkMap(final Method method) {
        return extractFromBulkMap(method, isComplexType());
    }

    public static Optional<Type> extractFromBulkPrimitive(final Method method) {
        return extractFromBulkMap(method, isPrimitiveType());
    }

    public static Optional<Type> extractFromSinglePrimitive(final Method method) {
        return extract(method, t -> {
            final TypeCaptor captor = new TypeCaptor();
            captor.captureIf(isPrimitiveType()).test(t);
            return captor.getCapturedType().orElse(null);
        });
    }
    public static Optional<Type> extractFromSingleComplex(final Method method) {
        return extract(method, t -> {
            final TypeCaptor captor = new TypeCaptor();
            captor.captureIf(isComplexType()).test(t);
            return captor.getCapturedType().orElse(null);
        });
    }

    private static Optional<Type> extractFromBulkMap(final Method method, final Predicate<Type> queryTypePredicate) {
        return extract(method, t -> {
            final TypeCaptor captor = new TypeCaptor();
            mapOf(assignableTo(String.class), captor.captureIf(queryTypePredicate)).test(t);
            return captor.getCapturedType().orElse(null);
        });

    }

    private static Optional<Type> extract(final Method method, final Function<Type, Type> extractor) {
        final Type[] types = method.getGenericParameterTypes();
        final Optional<Type> type = types.length == 1 ? Optional.of(types[0]) : Optional.empty();
        return type.map(extractor).filter(Objects::nonNull);
    }
}
