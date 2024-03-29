package com.dajudge.buql.analyzer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

public final class ReflectionUtil {
    private static final Collection<Class<?>> PRIMITIVE_TYPES = asList(
            String.class,
            byte.class, Byte.class,
            char.class, Character.class,
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class
    );

    private ReflectionUtil() {
    }

    private static Predicate<Type> mapOf(
            final Predicate<Type> keyTypePredicate,
            final Predicate<Type> valueTypePredicate
    ) {
        return mapType -> {
            if (!(mapType instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType parameterizedType = (ParameterizedType) mapType;
            if (Map.class != parameterizedType.getRawType()) {
                return false;
            }
            final Type keyType = parameterizedType.getActualTypeArguments()[0];
            final Type valueType = parameterizedType.getActualTypeArguments()[1];
            return keyTypePredicate.test(keyType) && valueTypePredicate.test(valueType);
        };
    }

    public static Predicate<Type> listOf(final Predicate<Type> valueTypePredicate) {
        return listType -> {
            if (!(listType instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType parameterizedType = (ParameterizedType) listType;
            if (List.class != parameterizedType.getRawType()) {
                return false;
            }
            return valueTypePredicate.test(parameterizedType.getActualTypeArguments()[0]);
        };
    }

    private static Predicate<Type> assignableTo(final Class<?> clazz) {
        return type -> {
            if (!(type instanceof Class)) {
                return false;
            }
            return clazz.isAssignableFrom((Class<?>) type);
        };
    }

    public static Predicate<Type> isPrimitiveType() {
        return type -> type instanceof Class && PRIMITIVE_TYPES.stream()
                .anyMatch(it -> it.isAssignableFrom((Class) type));
    }

    private static Predicate<Type> isClass() {
        return type -> type instanceof Class;
    }

    public static Predicate<Type> isComplexType() {
        return isClass().and(isPrimitiveType().negate());
    }

    public static Predicate<Type> mapOfStringTo(final Predicate<Type> v) {
        return mapOf(assignableTo(String.class), v);
    }

    public static class TypeCaptor {
        private Type type;

        public Predicate<Type> captureIf(final Predicate<Type> passThrough) {
            return type -> {
                final boolean result = passThrough.test(type);
                if (result) {
                    TypeCaptor.this.type = type;
                }
                return result;
            };
        }

        public Optional<Type> getCapturedType() {
            return Optional.ofNullable(type);
        }
    }
}
