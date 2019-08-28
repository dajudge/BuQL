package com.dajudge.buql.reflector;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResultMapper<R> {
    private final Supplier<R> factory;
    private final Map<String, BiConsumer<R, Object>> setters;
    private final String idCol;

    public ResultMapper(
            final Supplier<R> factory,
            final Map<String, BiConsumer<R, Object>> setters,
            final String idCol
    ) {
        this.factory = factory;
        this.setters = setters;
        this.idCol = idCol;
    }

    public String getId(final Function<String, Object> colAccessor) {
        return colAccessor.apply(idCol).toString();
    }

    public R getResultObject(final Function<String, Object> colAccessor) {
        final R resultObject = factory.get();
        setters.entrySet().forEach(e -> e.getValue().accept(resultObject, colAccessor.apply(e.getKey())));
        return resultObject;
    }
}
