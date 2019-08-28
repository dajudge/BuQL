package com.dajudge.buql.query.dialect;

import java.util.HashMap;
import java.util.Map;

public class IncrementingColRenamer implements ColRenamer {
    private final Map<String, String> forward = new HashMap<>();
    private final String prefix;
    private int counter;

    public IncrementingColRenamer(final String prefix, final int start) {
        this.prefix = prefix;
        counter = start;
    }

    @Override
    public String rename(final String in) {
        return map(in, false);
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
