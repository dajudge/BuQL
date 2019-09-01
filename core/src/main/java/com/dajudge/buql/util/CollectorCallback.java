package com.dajudge.buql.util;

import com.dajudge.buql.reflector.ReflectSelectQuery;

import java.util.*;

import static java.util.Collections.emptyList;

public class CollectorCallback<R> implements ReflectSelectQuery.Callback<R> {
    private final Map<String, List<R>> ret = new HashMap<>();
    private final Collection<String> queriedKeys;

    public CollectorCallback(final Collection<String> queriedKeys) {
        this.queriedKeys = queriedKeys;
    }

    @Override
    public void onResult(final String id, final R value) {
        ret.computeIfAbsent(id, k -> new ArrayList<>()).add(value);
    }

    @Override
    public void done() {
        queriedKeys.forEach(key -> ret.computeIfAbsent(key, k -> emptyList()));
    }

    @Override
    public void onError(final Exception e) {
        throw new RuntimeException(e);
    }

    public Map<String, List<R>> getResult() {
        return ret;
    }
}
