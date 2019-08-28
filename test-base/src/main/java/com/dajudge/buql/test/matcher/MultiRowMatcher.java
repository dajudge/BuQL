package com.dajudge.buql.test.matcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class MultiRowMatcher implements QueryResultAssertions.RowMatcher {
    private final QueryResultAssertions back;
    private final List<Map<String, Object>> rows;
    private final Map<String, Object> matches = new HashMap<>();

    public MultiRowMatcher(final QueryResultAssertions back, final List<Map<String, Object>> rows) {
        this.back = back;
        this.rows = rows;
    }

    @Override
    public QueryResultAssertions.RowMatcher hasColumun(final String columnName, final Object value) {
        matches.put(columnName, value);
        return this;
    }

    @Override
    public QueryResultAssertions endRow() {
        final Predicate<? super Map<String, Object>> matcher = row -> {
            for (final Map.Entry<String, Object> entry : matches.entrySet()) {
                if (!Objects.equals(entry.getValue(), row.get(entry.getKey()))) {
                    return false;
                }
            }
            return true;
        };
        rows.stream().filter(matcher)
                .findAny()
                .orElseThrow(() -> new AssertionError("Row not found: " + matches));
        return back;
    }
}
