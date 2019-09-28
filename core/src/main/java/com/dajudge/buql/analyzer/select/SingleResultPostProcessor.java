package com.dajudge.buql.analyzer.select;

import java.util.Map;
import java.util.function.Function;

import static com.dajudge.buql.analyzer.select.SingleQueryPreProcessor.QUERY_ID;

public class SingleResultPostProcessor implements Function<Map<String, ?>, Object> {

    @Override
    public Object apply(final Map<String, ?> map) {
        return map.get(QUERY_ID);
    }
}
