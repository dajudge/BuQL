package com.dajudge.buql.reflector.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FilterFieldsMapping {
    private final Map<String, Function<Object, Object>> resolver = new HashMap<>();
    private final Map<Function<Object, Object>, String> cache = new HashMap<>();

    public List<String> getFilterColumns() {
        return new ArrayList<>(resolver.keySet());
    }

    public String create(final Function<Object, Object> reader) {
        final String id = cache.computeIfAbsent(reader, r -> "P" + resolver.size());
        resolver.put(id, reader);
        return id;
    }

    public Map<String, Object> extractFilterFields(final Object object) {
        final Map<String, Object> ret = new HashMap<>();
        resolver.forEach((k, v) -> ret.put(k, v.apply(object)));
        return ret;
    }
}
