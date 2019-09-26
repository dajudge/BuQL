package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.GreaterThanEquals;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GreaterThanEqualsOperatorTest extends BuqlTest {
    private static final Map<String, GreaterThanEqualsQueryObject> PARAMS = new HashMap<String, GreaterThanEqualsQueryObject>() {{
        put("ID0", new GreaterThanEqualsQueryObject(42));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByGreaterThanEquals(Map<String, GreaterThanEqualsQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void greater_than_equals_query() {
        final Map<String, List<String>> result = repository.findStringValueByGreaterThanEquals(PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(2, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
        assertTrue(result.get("ID0").contains("v1"));
    }

    public static class GreaterThanEqualsQueryObject {
        private final long longValue;

        public GreaterThanEqualsQueryObject(final long longValue) {
            this.longValue = longValue;
        }

        @GreaterThanEquals
        public long getLongValue() {
            return longValue;
        }
    }
}
