package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.reflector.annotations.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleExampleTest extends BuqlTest {
    private static final HashMap<String, SimpleQueryObject> COMPLEX_PARAMS_ONE_RESULT = new HashMap<String, SimpleQueryObject>() {{
        put("ID0", new SimpleQueryObject(42));
    }};
    private static final HashMap<String, SimpleQueryObject> PARAMS_NO_RESULT = new HashMap<String, SimpleQueryObject>() {{
        put("ID0", new SimpleQueryObject(4711));
    }};
    private static final HashMap<String, Long> PRIMITIVE_PARAMS_ONE_RESULT = new HashMap<String, Long>() {{
        put("ID0", 42l);
    }};
    private static final Map<String, PatternQueryObject> PATTERN_PARAMS = new HashMap<String, PatternQueryObject>() {{
        put("ID0", new PatternQueryObject("v%"));
    }};
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<FullResultObject>> findByThing1(Map<String, SimpleQueryObject> query);

        Map<String, FullResultObject> getByThing1(Map<String, SimpleQueryObject> query);

        Map<String, FullResultObject> getByLongValue(Map<String, Long> query);

        Map<String, List<String>> findStringValueByPattern(Map<String, PatternQueryObject> query);

        Map<String, String> getStringValueByLongValue(Map<String, Long> query);
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

    @Test
    public void bulk_primitive_to_unique_complex() {
        final Map<String, FullResultObject> result = repository.getByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertNotNull("v0", result.get("ID0").getStringValue());
    }

    @Test
    public void bulk_complex_to_many_complex() {
        final Map<String, List<FullResultObject>> result = repository.findByThing1(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0).getStringValue());
    }

    @Test
    public void bulk_complex_to_unique_complex() {
        final Map<String, FullResultObject> result = repository.getByThing1(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals("v0", result.get("ID0").getStringValue());
    }

    @Test
    public void bulk_primitive_to_unique_primitive() {
        final Map<String, String> result = repository.getStringValueByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals("v0", result.get("ID0"));
    }

    @Test
    public void lists_empty_results() {
        final Map<String, List<FullResultObject>> result = repository.findByThing1(PARAMS_NO_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertTrue(result.get("ID0").isEmpty());
    }
}
