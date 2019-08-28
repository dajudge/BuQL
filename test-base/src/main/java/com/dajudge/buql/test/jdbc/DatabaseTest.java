package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.engine.jdbc.JdbcDatabaseEngine;
import com.dajudge.buql.test.TestEnvironment;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class DatabaseTest {
    private Connection connection;
    JdbcDatabaseEngine engine;
    Dialect dialect;

    @Before
    public void setup() throws SQLException {
        final TestEnvironment testEnvironment = ServiceLoader.load(TestEnvironment.class).iterator().next();
        connection = testEnvironment.newConnection();
        dialect = testEnvironment.getDialect();
        connection.setAutoCommit(false);
        engine = new JdbcDatabaseEngine(connectionConsumer -> connectionConsumer.accept(connection));
        final DefaultDatabaseResultCallback failOnError = new DefaultDatabaseResultCallback() {
            @Override
            public void onError(final SQLException e) {
                throw new RuntimeException("Failed to execute setup SQL", e);
            }
        };
        engine.executeStatement(
                "CREATE TABLE mytable(pk BIGINT, stringValue VARCHAR(128), longValue BIGINT, PRIMARY KEY(pk))",
                emptyList(),
                failOnError
        );
        asList(
                asList(0, "v0", 42),
                asList(1, "v1", 43)
        ).forEach(params -> engine.executeStatement(
                "INSERT INTO mytable(pk, stringValue, longValue) VALUES (?, ?, ?)",
                params,
                failOnError
        ));
    }

    @After
    public void teardown() throws SQLException {
        connection.close();
    }

}
