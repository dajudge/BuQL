package com.dajudge.buql.analyzer.select;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SingleQueryPreProcessor<Q> implements Function<Object, Map<String, Q>> {
    public static final String QUERY_ID = "ID";

    @Override
    public Map<String, Q> apply(final Object o) {
        return new HashMap<String, Q>() {{
            put(QUERY_ID, (Q) o);
        }};
    }
}
