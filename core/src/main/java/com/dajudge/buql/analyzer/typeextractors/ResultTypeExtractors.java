package com.dajudge.buql.analyzer.typeextractors;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.typeextractors.ReflectionUtil.*;

public final class ResultTypeExtractors {
    private ResultTypeExtractors() {
    }

    public static Optional<Type> extractToComplexManyMap(final Method method) {
        return extractToManyMap(method, isComplexType());
    }

    public static Optional<Type> extractToComplexUniqueMap(final Method method) {
        return extractToUniqueMap(method, isComplexType());
    }

    public static Optional<Type> extractToPrimitiveUniqueMap(final Method method) {
        return extractToUniqueMap(method, isPrimitiveType());
    }

    public static Optional<Type> extractToPrimitiveManyMap(final Method method) {
        return extractToManyMap(method, isPrimitiveType());
    }

    public static Optional<Type> extractToPrimitiveSingle(final Method method) {
        return isPrimitiveType().test(method.getGenericReturnType()) ?
                Optional.of(method.getGenericReturnType()) :
                Optional.empty();
    }

    public static Optional<Type> extractToComplexSingle(final Method method) {
        return isComplexType().test(method.getGenericReturnType()) ?
                Optional.of(method.getGenericReturnType()) :
                Optional.empty();
    }

    public static Optional<Type> extractToComplexMany(final Method method) {
        return extractToMany(method, isComplexType());
    }

    public static Optional<Type> extractToPrimitiveMany(final Method method) {
        return extractToMany(method, isPrimitiveType());
    }

    private static Optional<Type> extractToUniqueMap(final Method method, final Predicate<Type> resultTypePredicate) {
        final TypeCaptor captor = new TypeCaptor();
        final Type returnType = method.getGenericReturnType();
        mapOf(assignableTo(String.class), captor.captureIf(resultTypePredicate)).test(returnType);
        return captor.getCapturedType();
    }

    private static Optional<Type> extractToManyMap(final Method method, final Predicate<Type> resultTypePredicate) {
        final TypeCaptor captor = new TypeCaptor();
        final Type returnType = method.getGenericReturnType();
        mapOf(assignableTo(String.class), listOf(captor.captureIf(resultTypePredicate))).test(returnType);
        return captor.getCapturedType();
    }

    private static Optional<Type> extractToMany(final Method method, final Predicate<Type> resultTypePredicate) {
        final TypeCaptor captor = new TypeCaptor();
        final Type returnType = method.getGenericReturnType();
        listOf(captor.captureIf(resultTypePredicate)).test(returnType);
        return captor.getCapturedType();
    }
}
