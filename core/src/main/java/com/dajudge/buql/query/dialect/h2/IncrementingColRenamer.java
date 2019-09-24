package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.ColRenamer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncrementingColRenamer implements ColRenamer {
    private final Map<String, String> forward = new HashMap<>();
    private final String prefix;
    private int counter;

    public IncrementingColRenamer(final String prefix, final int start, final List<String> colNames) {
        this.prefix = prefix;
        counter = start;
        colNames.stream().forEach(colName -> map(colName, false));
    }

    private String map(final String in, final boolean required) {
        final String existing = forward.get(in);
        if (existing != null) {
            return existing;
        } else if (required) {
            throw new IllegalArgumentException("No such renamed column: " + in);
        }
        final String nextOut = prefix + counter++;
        forward.put(in, nextOut);
        return nextOut;
    }

    @Override
    public String resolve(final String in) {
        return map(in, true);
    }
}
