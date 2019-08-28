package com.dajudge.buql.util;

import com.dajudge.buql.reflector.ReflectSelectQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectorCallback<R> implements ReflectSelectQuery.Callback<R> {
    final Map<String, List<R>> ret = new HashMap<>();

    @Override
    public void onResult(final String id, final R value) {
        ret.computeIfAbsent(id, k -> new ArrayList<>()).add(value);
    }

    @Override
    public void done() {
    }

    @Override
    public void onError(final Exception e) {
        throw new RuntimeException(e);
    }

    public Map<String, List<R>> getResult() {
        return null;
    }
}
