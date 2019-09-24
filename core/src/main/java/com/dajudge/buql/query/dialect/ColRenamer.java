package com.dajudge.buql.query.dialect;

import java.util.List;
import java.util.Map;

public interface ColRenamer {
    ColRenamer IDENTITY = in -> in;

    static ColRenamer prefixRenamer(final String prefix, final List<String> cols) {
        final Map<String, String> resolver;
        return new ColRenamer() {
            @Override
            public String resolve(final String in) {
                return null;
            }
        };
    }

    String resolve(String in);
}
