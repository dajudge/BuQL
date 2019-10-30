package com.dajudge.buql.analyzer.insert;

import java.util.List;
import java.util.function.Function;

public class BulkInsertPreProcessor<Q> implements Function<Object, List<Q>> {
    @Override
    public List<Q> apply(final Object o) {
        return (List<Q>) o;
    }
}
