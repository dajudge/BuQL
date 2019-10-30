package com.dajudge.buql.test.analyzers;

import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class InsertAnalyzersTest extends BuqlTest {

    private InsertTestRepository repository;

    @Before
    public void setup() {
        repository = buql.up(InsertTestRepository.class);
    }

    @Test
    public void inserts_multiple() {
        final List<FullObject> objectsToCreate = asList(
                new FullObject(10L, "stringValue10", 52L, "nullableValue10"),
                new FullObject(11L, "stringValue11", 53L, null)
        );
        repository.create(objectsToCreate);

        final Map<String, FullObject> results = repository.getByPk(new HashMap<String, Long>() {{
            put("ID0", objectsToCreate.get(0).getPk());
            put("ID1", objectsToCreate.get(1).getPk());
        }});
        assertEquals(2, results.size());
        assertEquals(objectsToCreate.get(0), results.get("ID0"));
        assertEquals(objectsToCreate.get(1), results.get("ID1"));
    }

    @Test
    public void inserts_single() {
        final FullObject objectToCreate = new FullObject(10L, "stringValue10", 52L, "nullableValue10");

        repository.create(objectToCreate);

        final Map<String, FullObject> results = repository.getByPk(new HashMap<String, Long>() {{
            put("ID0", objectToCreate.getPk());
        }});
        assertEquals(1, results.size());
        assertEquals(objectToCreate, results.get("ID0"));
    }

    @Table("mytable")
    public interface InsertTestRepository {
        void create(final List<FullObject> objects);

        void create(final FullObject object);

        Map<String, FullObject> getByPk(final Map<String, Long> pks);
    }

    public static class FullObject {
        private long pk;
        private String stringValue;
        private long longValue;
        private String nullableValue;

        public FullObject() {
        }

        public FullObject(final long pk, final String stringValue, final long longValue, final String nullableValue) {
            this.pk = pk;
            this.stringValue = stringValue;
            this.longValue = longValue;
            this.nullableValue = nullableValue;
        }

        public long getPk() {
            return pk;
        }

        public void setPk(final long pk) {
            this.pk = pk;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(final String stringValue) {
            this.stringValue = stringValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(final long longValue) {
            this.longValue = longValue;
        }

        public String getNullableValue() {
            return nullableValue;
        }

        public void setNullableValue(final String nullableValue) {
            this.nullableValue = nullableValue;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final FullObject that = (FullObject) o;
            return pk == that.pk &&
                    longValue == that.longValue &&
                    Objects.equals(stringValue, that.stringValue) &&
                    Objects.equals(nullableValue, that.nullableValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pk, stringValue, longValue, nullableValue);
        }

        @Override
        public String toString() {
            return "FullObject{" +
                    "pk=" + pk +
                    ", stringValue='" + stringValue + '\'' +
                    ", longValue=" + longValue +
                    ", nullableValue='" + nullableValue + '\'' +
                    '}';
        }
    }
}
