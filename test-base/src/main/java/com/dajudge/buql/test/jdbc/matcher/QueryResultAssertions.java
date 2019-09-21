package com.dajudge.buql.test.jdbc.matcher;

import static org.junit.Assert.*;

public class QueryResultAssertions {
    private final QueryResult result;

    public static QueryResultAssertions assertWith(final QueryResult result) {
        return new QueryResultAssertions(result);
    }

    private QueryResultAssertions(final QueryResult result) {
        this.result = result;
    }

    public QueryResultAssertions hasCount(final int count) {
        assertEquals("Result set has wrong size", count, result.getRows().size());
        return this;
    }

    public RowMatcher row(final int index) {
        return new SimpleRowMatcher(this, result.getRows().get(index));
    }

    public QueryResultAssertions hasException() {
        assertNotNull("Expected SQLException", result.getException());
        return this;
    }

    public QueryResultAssertions hasNoException() {
        assertNull("Unexpected SQLException", result.getException());
        return this;
    }

    public RowMatcher hasRow() {
        return new MultiRowMatcher(this, result.getRows());
    }

    public interface RowMatcher {
        RowMatcher hasColumun(final String columnName, final Object value);

        QueryResultAssertions endRow();
    }
}
