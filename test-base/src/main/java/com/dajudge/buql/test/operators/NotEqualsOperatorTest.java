package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.NotEquals;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NotEqualsOperatorTest extends BuqlTest {
    private static final Map<String, NotEqualsQueryObject> PARAMS = new HashMap<String, NotEqualsQueryObject>() {{
        put("ID0", new NotEqualsQueryObject(43));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByNotEquals(Map<String, NotEqualsQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void not_equals_query() {
        final Map<String, List<String>> result = repository.findStringValueByNotEquals(PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
    }

    public static class NotEqualsQueryObject {
        private final long longValue;

        public NotEqualsQueryObject(final long longValue) {
            this.longValue = longValue;
        }

        @NotEquals
        public long getLongValue() {
            return longValue;
        }
    }
}
