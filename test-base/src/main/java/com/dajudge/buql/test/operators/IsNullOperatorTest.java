package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.Nullable;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsNullOperatorTest extends BuqlTest {
    private static final Map<String, LessThanQueryObject> PARAMS = new HashMap<String, LessThanQueryObject>() {{
        put("ID0", new LessThanQueryObject(null));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByNullable(Map<String, LessThanQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void nullable_query() {
        final Map<String, List<String>> result = repository.findStringValueByNullable(PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
    }

    public static class LessThanQueryObject {
        private final String nullableValue;

        public LessThanQueryObject(final String nullableValue) {
            this.nullableValue = nullableValue;
        }

        @Nullable
        public String getNullableValue() {
            return nullableValue;
        }
    }
}
