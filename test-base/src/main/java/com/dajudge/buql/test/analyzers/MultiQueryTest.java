package com.dajudge.buql.test.analyzers;

import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.*;

public class MultiQueryTest extends BuqlTest {
    private SomeObjectRepository repository;

    @Table("mytable")
    interface SomeObjectRepository {
        Map<String, String> getStringValueByLongValue(Map<String, Long> query);
    }

    @Before
    public void setup() {
        repository = buql.up(SomeObjectRepository.class);
    }

    @Test
    public void query_with_large_number_of_params() {
        final Map<String, Long> map = new HashMap<>();
        for (long i = 0; i < 100000; i++) {
            map.put("ID" + i, i);
        }
        try {
            final Map<String, String> result = repository.getStringValueByLongValue(map);
            assertNotNull(result);
            final List<String> hits = result.entrySet().stream()
                    .filter(it -> it.getValue() != null)
                    .map(it -> it.getKey())
                    .collect(toList());
            assertEquals(2, hits.size());
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }
}
