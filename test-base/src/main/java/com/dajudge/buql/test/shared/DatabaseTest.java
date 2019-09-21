package com.dajudge.buql.test.shared;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.engine.jdbc.JdbcDatabaseEngine;
import com.dajudge.buql.test.TestEnvironment;
import com.dajudge.buql.test.shared.h2.H2TestEnvironment;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.ServiceLoader.load;

public abstract class DatabaseTest {
    private Connection connection;
    protected JdbcDatabaseEngine engine;
    protected Dialect dialect;

    @Before
    public void setupDatabase() throws SQLException {
        final ServiceLoader<TestEnvironment> loader = load(TestEnvironment.class);
        final TestEnvironment testEnvironment = StreamSupport.stream(loader.spliterator(), false)
                .findFirst()
                .orElse(new H2TestEnvironment());
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
