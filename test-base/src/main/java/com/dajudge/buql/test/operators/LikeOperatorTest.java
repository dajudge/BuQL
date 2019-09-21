package com.dajudge.buql.test.operators;

import com.dajudge.buql.reflector.annotations.Like;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LikeOperatorTest extends BuqlTest {
    private static final Map<String, LikeQueryObject> PATTERN_PARAMS = new HashMap<String, LikeQueryObject>() {{
        put("ID0", new LikeQueryObject("v%"));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<String>> findStringValueByPattern(Map<String, LikeQueryObject> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void like_query() {
        final Map<String, List<String>> result = repository.findStringValueByPattern(PATTERN_PARAMS);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(2, result.get("ID0").size());
        assertTrue(result.get("ID0").contains("v0"));
        assertTrue(result.get("ID0").contains("v1"));
    }

    public static class LikeQueryObject {
        private final String stringValue;

        public LikeQueryObject(final String stringValue) {
            this.stringValue = stringValue;
        }

        @Like
        public String getStringValue() {
            return stringValue;
        }
    }
}
