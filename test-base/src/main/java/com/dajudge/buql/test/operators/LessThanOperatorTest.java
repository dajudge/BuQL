package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.LessThan;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LessThanOperatorTest extends BuqlTest {
    private static final Map<String, LessThanQueryObject> PARAMS = new HashMap<String, LessThanQueryObject>() {{
        put("ID0", new LessThanQueryObject(43));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByLessThan(Map<String, LessThanQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void greater_than_query() {
        final Map<String, List<String>> result = repository.findStringValueByLessThan(PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
    }

    public static class LessThanQueryObject {
        private final long longValue;

        public LessThanQueryObject(final long longValue) {
            this.longValue = longValue;
        }

        @LessThan
        public long getLongValue() {
            return longValue;
        }
    }
}
