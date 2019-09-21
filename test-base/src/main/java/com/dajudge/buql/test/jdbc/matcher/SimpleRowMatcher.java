package com.dajudge.buql.test.jdbc.matcher;

import com.dajudge.buql.test.jdbc.matcher.QueryResultAssertions.RowMatcher;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleRowMatcher implements RowMatcher {
    private final QueryResultAssertions back;
    private final Map<String, Object> row;

    public SimpleRowMatcher(final QueryResultAssertions back, final Map<String, Object> row) {
        this.back = back;
        this.row = row;
    }

    @Override
    public RowMatcher hasColumun(final String columnName, final Object value) {
        assertTrue("Row " + row + " has no column '" + columnName + "'", row.containsKey(columnName));
        assertEquals("Column '" + columnName + "' has wrong value", value, row.get(columnName));
        return this;
    }

    @Override
    public QueryResultAssertions endRow() {
        return back;
    }
}
