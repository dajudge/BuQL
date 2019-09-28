package com.dajudge.buql.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UniquePostProcessor<R> implements Function<Map<String, List<R>>, Map<String, ?>> {
    @Override
    public Map<String, R> apply(final Map<String, List<R>> results) {
        final Map<String, R> ret = new HashMap<>();
        results.entrySet().stream()
                .peek(entry -> {
                    if (entry.getValue().size() > 1) {
                        throw new IllegalStateException("Multiple result rows for unique result.");
                    }
                }).forEach(entry -> {
            ret.put(entry.getKey(), entry.getValue().stream().findFirst().orElse(null));
        });
        return ret;
    }
}
