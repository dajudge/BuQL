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
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<FullResultObject>> findByThing1(Map<String, SimpleQueryObject> query);

        Map<String, FullResultObject> getByThing1(Map<String, SimpleQueryObject> query);

        Map<String, FullResultObject> getByLongValue(Map<String, Long> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void primitive_bulk_to_complex_unique() {
        final Map<String, FullResultObject> result = repository.getByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertNotNull("v0", result.get("ID0").getStringValue());
    }

    @Test
    public void complex_bulk_to_complex_many() {
        final Map<String, List<FullResultObject>> result = repository.findByThing1(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0).getStringValue());
    }

    @Test
    public void complex_bulk_to_complex_unique() {
        final Map<String, FullResultObject> result = repository.getByThing1(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals("v0", result.get("ID0").getStringValue());
    }

    @Test
    public void lists_empty_results() {
        final Map<String, List<FullResultObject>> result = repository.findByThing1(PARAMS_NO_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertTrue(result.get("ID0").isEmpty());
    }
}
