package com.dajudge.buql.analyzer;

import java.util.Map;
import java.util.function.Function;

public class SingleResultPostProcessor<T> implements Function<Map<String, T>, T> {
    private final String id;

    public SingleResultPostProcessor(final String id) {
        this.id = id;
    }

    @Override
    public T apply(final Map<String, T> map) {
        return map.get(id);
    }
}
