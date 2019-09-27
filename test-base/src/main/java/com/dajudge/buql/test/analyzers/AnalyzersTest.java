package com.dajudge.buql.test.analyzers;

import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import com.dajudge.buql.test.shared.model.FullResultObject;
import com.dajudge.buql.test.shared.model.SimpleQueryObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AnalyzersTest extends BuqlTest {
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
        Map<String, FullResultObject> getByComplexQuery(Map<String, SimpleQueryObject> query);

        Map<String, Long> getLongValueByComplexQuery(Map<String, SimpleQueryObject> query);

        Map<String, List<FullResultObject>> findByComplexQuery(Map<String, SimpleQueryObject> query);

        Map<String, List<String>> findStringValueByComplexQuery(Map<String, SimpleQueryObject> query);


        Map<String, FullResultObject> getByLongValue(Map<String, Long> query);

        Map<String, String> getStringValueByLongValue(Map<String, Long> query);

        Map<String, List<FullResultObject>> findByLongValue(Map<String, Long> query);

        Map<String, List<String>> findStringValueByLongValue(Map<String, Long> query);


        String getStringValueByLongValue(long query);

        FullResultObject getByLongValue(long query);

        List<FullResultObject> findByStringValue(String query);

        List<String> findStringValueByLongValue(long query);


        String getStringValueByComplexQuery(SimpleQueryObject query);

        FullResultObject getByComplexQuery(SimpleQueryObject query);

        List<FullResultObject> findByComplexQuery(SimpleQueryObject query);

        List<String> findStringValueByComplexQuery(SimpleQueryObject query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void single_complex_to_many_primitive() {
        final List<String> result = repository.findStringValueByComplexQuery(COMPLEX_PARAMS_ONE_RESULT.get("ID0"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("v0", result.get(0));
    }

    @Test
    public void single_complex_to_many_complex() {
        final List<FullResultObject> result = repository.findByComplexQuery(COMPLEX_PARAMS_ONE_RESULT.get("ID0"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(42l, result.get(0).getLongValue());
    }

    @Test
    public void single_complex_to_unique_complex() {
        final FullResultObject result = repository.getByComplexQuery(COMPLEX_PARAMS_ONE_RESULT.get("ID0"));

        assertNotNull(result);
        assertEquals("v0", result.getStringValue());
    }

    @Test
    public void single_complex_to_unique_primitive() {
        final String result = repository.getStringValueByComplexQuery(COMPLEX_PARAMS_ONE_RESULT.get("ID0"));

        assertNotNull(result);
        assertEquals("v0", result);
    }

    @Test
    public void single_primitive_to_many_primitive() {
        final List<String> result = repository.findStringValueByLongValue(42);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("v0", result.get(0));
    }

    @Test
    public void single_primitive_to_many_complex() {
        final List<FullResultObject> result = repository.findByStringValue("v0");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(42l, result.get(0).getLongValue());
    }

    @Test
    public void single_primitive_to_unique_complex() {
        final FullResultObject result = repository.getByLongValue(42);

        assertNotNull(result);
        assertEquals("v0", result.getStringValue());
    }

    @Test
    public void single_primitive_to_unique_primitive() {
        final String result = repository.getStringValueByLongValue(42);

        assertNotNull(result);
        assertEquals("v0", result);
    }

    @Test
    public void bulk_complex_to_many_primitive() {
        final Map<String, List<String>> result = repository.findStringValueByComplexQuery(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0));
    }

    @Test
    public void bulk_complex_to_unqiue_primitive() {
        final Map<String, Long> result = repository.getLongValueByComplexQuery(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(42l, (long)result.get("ID0"));
    }

    @Test
    public void bulk_primitive_to_unique_complex() {
        final Map<String, FullResultObject> result = repository.getByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals("v0", result.get("ID0").getStringValue());
    }

    @Test
    public void bulk_primitive_to_many_complex() {
        final Map<String, List<FullResultObject>> result = repository.findByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0).getStringValue());
    }

    @Test
    public void bulk_complex_to_many_complex() {
        final Map<String, List<FullResultObject>> result = repository.findByComplexQuery(COMPLEX_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0).getStringValue());
    }

    @Test
    public void bulk_complex_to_unique_complex() {
        final Map<String, FullResultObject> result = repository.getByComplexQuery(COMPLEX_PARAMS_ONE_RESULT);

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
    public void bulk_primitive_to_many_primitive() {
        final Map<String, List<String>> result = repository.findStringValueByLongValue(PRIMITIVE_PARAMS_ONE_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0));
    }

    @Test
    public void lists_empty_results() {
        final Map<String, List<FullResultObject>> result = repository.findByComplexQuery(PARAMS_NO_RESULT);

        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertTrue(result.get("ID0").isEmpty());
    }
}
