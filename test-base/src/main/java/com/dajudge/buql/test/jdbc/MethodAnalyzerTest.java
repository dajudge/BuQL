package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.reflector.model.MethodSelectModel;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;

import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;
import static java.lang.reflect.Proxy.newProxyInstance;

public class MethodAnalyzerTest extends DatabaseTest {

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<FullResultObject>> findByThing1(Map<String, SimpleQueryObject> query);
    }

    @Test
    public void translates_method_definition() {
        final SomeObjectRepository repository = buqlUp(SomeObjectRepository.class, dialect, engine);
        final Map<String, SimpleQueryObject> params = new HashMap<String, SimpleQueryObject>() {{
            put("ID0", new SimpleQueryObject(42));
        }};
        final Map<String, List<FullResultObject>> result = repository.findByThing1(params);
        System.out.println(result);
    }

    private <T> T buqlUp(final Class<T> iface, final Dialect dialect, final DatabaseEngine databaseEngine) {
        final Table tableAnnotation = iface.getAnnotation(Table.class);
        final Object proxy = newProxyInstance(getClass().getClassLoader(), new Class[]{iface}, (o, method, objects) -> {
            final Map<String, List<Object>> ret = new HashMap<>();
            final ReflectSelectQuery.Callback cb = new ReflectSelectQuery.Callback<Object>() {
                @Override
                public void onResult(final String id, final Object value) {
                    ret.computeIfAbsent(id, k -> new ArrayList<>()).add(value);
                }

                @Override
                public void done() {
                }

                @Override
                public void onError(final Exception e) {
                    throw new RuntimeException(e);
                }
            };
            final Map params = (Map<String, ?>) objects[0];
            analyze(tableAnnotation.value(), method).get().execute(dialect, databaseEngine, params, cb);
            return ret;
        });
        return (T) proxy;
    }

    private Optional<ReflectSelectQuery<?, ?>> analyze(final String tableName, final Method method) {
        final Optional<Type> queryType = extractQueryType(method);
        final Optional<Type> resultType = extractResultType(method);
        if (!matchesName(method) && queryType.isPresent() && resultType.isPresent()) {
            return Optional.empty();
        }
        final MethodSelectModel<?, ?> model = createSelectModel(
                tableName,
                (Class<?>) queryType.get(),
                (Class<?>) resultType.get()
        );
        return Optional.of(translateMethodModelToQuery(model));
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

    private static Predicate<Type> listOf(final Predicate<Type> valueTypePredicate) {
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

    private static Predicate<Type> isClass() {
        return type -> type instanceof Class;
    }

    private static class TypeCaptor {
        private Type type;

        public Predicate<Type> capture() {
            return captureIf(t -> true);
        }

        public Predicate<Type> captureIf(final Predicate passThrough) {
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

    private static Optional<Type> extractQueryType(final Method method) {
        final Type[] types = method.getGenericParameterTypes();
        if (types.length != 1) {
            return Optional.empty();
        }
        final TypeCaptor captor = new TypeCaptor();
        mapOf(assignableTo(String.class), captor.captureIf(isClass()))
                .test(types[0]);
        return captor.getCapturedType();
    }

    private static Optional<Type> extractResultType(final Method method) {
        final TypeCaptor captor = new TypeCaptor();
        mapOf(assignableTo(String.class), listOf(captor.captureIf(isClass())))
                .test(method.getGenericReturnType());
        return captor.getCapturedType();
    }

    private static boolean matchesName(final Method method) {
        return method.getName().matches("findBy[A-Z].*");
    }
}
