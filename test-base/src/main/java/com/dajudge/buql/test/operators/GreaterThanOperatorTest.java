package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.GreaterThan;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GreaterThanOperatorTest extends BuqlTest {
    private static final Map<String, GreaterThanQueryObject> PARAMS = new HashMap<String, GreaterThanQueryObject>() {{
        put("ID0", new GreaterThanQueryObject(42));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByGreaterThan(Map<String, GreaterThanQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void greater_than_query() {
        final Map<String, List<String>> result = repository.findStringValueByGreaterThan(PARAMS);
System.out.println(result);
        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v1"));
    }

    public static class GreaterThanQueryObject {
        private final long longValue;

        public GreaterThanQueryObject(final long longValue) {
            this.longValue = longValue;
        }

        @GreaterThan
        public long getLongValue() {
            return longValue;
        }
    }
}
