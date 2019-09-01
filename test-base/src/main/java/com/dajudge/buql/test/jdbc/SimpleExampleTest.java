package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.reflector.annotations.Table;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleExampleTest extends BuqlTest {

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, List<FullResultObject>> findByThing1(Map<String, SimpleQueryObject> query);
    }

    @Test
    public void translates_method_definition() {
        // Create the BuQL backed repository instance
        final SomeObjectRepository repository = buql.up(SomeObjectRepository.class);

        // Defined the query parameters
        final Map<String, SimpleQueryObject> params = new HashMap<String, SimpleQueryObject>() {{
            put("ID0", new SimpleQueryObject(42));
        }};

        // Execute the query
        final Map<String, List<FullResultObject>> result = repository.findByThing1(params);

        // Let's see it!
        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("v0", result.get("ID0").get(0).getStringValue());
    }

    @Test
    public void lists_empty_results() {
        final Map<String, List<FullResultObject>> result = buql.up(SomeObjectRepository.class)
                .findByThing1(new HashMap<String, SimpleQueryObject>() {{
                    put("ID0", new SimpleQueryObject(4711));
                }});
        assertNotNull(result);
        assertNotNull(result.get("ID0"));
        assertTrue(result.get("ID0").isEmpty());
    }
}
