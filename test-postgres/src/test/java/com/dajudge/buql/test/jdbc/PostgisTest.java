package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.postgis.annotation.Covers;
import com.dajudge.buql.postgis.types.Point;
import com.dajudge.buql.reflector.annotations.Column;
import com.dajudge.buql.reflector.annotations.Table;
import com.dajudge.buql.test.shared.BuqlTest;
import com.dajudge.buql.test.shared.TestContainer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.dajudge.buql.test.shared.TestContainer.runDatabaseInitializer;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PostgisTest extends BuqlTest {
    @BeforeClass
    public static void setupTestData() throws SQLException {
        runDatabaseInitializer(engine -> {
            final String cols = Stream.of(
                    "id BIGINT NOT NULL",
                    "name VARCHAR(256) NOT NULL",
                    "poly GEOMETRY NOT NULL"
            ).collect(joining(", "));
            final String ddl = "CREATE TABLE polys(" + cols + ", PRIMARY KEY(id))";
            engine.executeStatement(ddl, emptyList(), FAIL_ON_ERROR);

            final String insert = "INSERT INTO polys(id, name, poly) VALUES(?, ?, ST_GeomFromText(?))";
            engine.executeStatement(insert, asList(1l, "dana", "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))"), FAIL_ON_ERROR);
            engine.executeStatement(insert, asList(2l, "bill", "POLYGON((2 0, 3 0, 3 1, 2 1, 2 0))"), FAIL_ON_ERROR);
        });
    }

    public static class PolyResultObject {
        private String name;
        private long id;

        public PolyResultObject() {
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setId(final long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }
    }

    public static class CoversQueryObject {
        private final Point point;

        public CoversQueryObject(final Point point) {
            this.point = point;
        }

        @Covers
        @Column("poly")
        public Point getPoint() {
            return point;
        }
    }

    @Table("polys")
    interface GeoRepository {
        Map<String, List<PolyResultObject>> findByPoint(final Map<String, CoversQueryObject> query);
    }

    private GeoRepository repository;

    @Before
    public void setup() {
        repository = buql.up(GeoRepository.class);
    }

    @Test
    public void covers() {
        final HashMap<String, CoversQueryObject> query = new HashMap<String, CoversQueryObject>() {{
            put("ID0", new PostgisTest.CoversQueryObject(new Point(.5, .5)));
        }};
        final Map<String, List<PolyResultObject>> result = repository.findByPoint(query);
        assertNotNull(result.get("ID0"));
        assertEquals(1, result.get("ID0").size());
        assertEquals("dana", result.get("ID0").get(0).getName());
    }
}
