package com.dajudge.buql.analyzer.insert;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;

public class SingleInsertPreProcessor<Q> implements Function<Object, List<Q>> {
    @Override
    public List<Q> apply(final Object o) {
        return singletonList((Q) o);
    }
}
