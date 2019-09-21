package com.dajudge.buql.analyzer;

import java.util.Map;
import java.util.function.Function;

public class BulkPreProcessor<Q> implements Function<Object, Map<String, Q>> {
    @Override
    public Map<String, Q> apply(final Object o) {
        return (Map<String, Q>) o;
    }
}
