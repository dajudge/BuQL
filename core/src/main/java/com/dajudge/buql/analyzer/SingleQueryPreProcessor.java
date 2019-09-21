package com.dajudge.buql.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SingleQueryPreProcessor<Q> implements Function<Object, Map<String, Q>> {
    private final String id;

    public SingleQueryPreProcessor(final String id) {
        this.id = id;
    }

    @Override
    public Map<String, Q> apply(final Object o) {
        return new HashMap<String, Q>() {{
            put(id, (Q) o);
        }};
    }
}
