package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.reflector.annotations.Table;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println(result);
    }


}
