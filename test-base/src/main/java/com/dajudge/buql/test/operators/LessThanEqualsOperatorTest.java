package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.LessThanEquals;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LessThanEqualsOperatorTest extends BuqlTest {
    private static final Map<String, LessThanEqualsQueryObject> PARAMS = new HashMap<String, LessThanEqualsQueryObject>() {{
        put("ID0", new LessThanEqualsQueryObject(43));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByLessThanEquals(Map<String, LessThanEqualsQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void less_than_equals_query() {
        final Map<String, List<String>> result = repository.findStringValueByLessThanEquals(PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(2, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
        assertTrue(result.get("ID0").contains("v1"));
    }

    public static class LessThanEqualsQueryObject {
        private final long longValue;

        public LessThanEqualsQueryObject(final long longValue) {
            this.longValue = longValue;
        }

        @LessThanEquals
        public long getLongValue() {
            return longValue;
        }
    }
}
