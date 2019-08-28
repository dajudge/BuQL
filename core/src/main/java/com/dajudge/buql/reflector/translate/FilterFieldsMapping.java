package com.dajudge.buql.reflector.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FilterFieldsMapping {
    private final Map<String, Function<Object, Object>> readers = new HashMap<>();

    public List<String> getFilterColumns() {
        return new ArrayList<>(readers.keySet());
    }

    public String create(final Function<Object, Object> reader) {
        final String id = "P" + readers.size();
        readers.put(id, reader);
        return id;
    }

    public Map<String, Object> extractFilterFields(final Object object) {
        return readers.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().apply(object)
        ));
    }
}
