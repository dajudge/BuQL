package com.dajudge.buql.test.shared;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.jdbc.JdbcDatabaseEngine;
import com.dajudge.buql.test.TestEnvironment;
import com.dajudge.buql.test.shared.h2.H2TestEnvironment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.joining;

public class TestContainer {

    private static final TestEnvironment TEST_ENVIRONMENT = getTestEnvironment();
    public static final Dialect DIALECT = TEST_ENVIRONMENT.getDialect();

    static {
        try {
            final Connection keepaliveConnection = TEST_ENVIRONMENT.newConnection();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    keepaliveConnection.close();
                } catch (final SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
            runDatabaseInitializer(TestContainer::setupTestData);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runDatabaseInitializer(final Consumer<JdbcDatabaseEngine> setupCallback) throws SQLException {
        try (final Connection setupConnection = TestContainer.newConnection()) {
            final JdbcDatabaseEngine initEngine = new JdbcDatabaseEngine(connectionConsumer ->
                    connectionConsumer.accept(setupConnection)
            );
            setupCallback.accept(initEngine);
        }
    }

    public static Connection newConnection() throws SQLException {
        return TEST_ENVIRONMENT.newConnection();
    }

    private static void setupTestData(final DatabaseEngine engine) {
        final String cols = Stream.of(
                "pk BIGINT",
                "stringValue VARCHAR(128) NOT NULL",
                "longValue BIGINT NOT NULL",
                "nullableValue VARCHAR(128)"
        ).collect(joining(", "));
        engine.executeStatement(
                "CREATE TABLE mytable(" + cols + ", PRIMARY KEY(pk))",
                emptyList(),
                DatabaseTest.FAIL_ON_ERROR
        );
        asList(
                asList(0, "v0", 42, null),
                asList(1, "v1", 43, "not null")
        ).forEach(params -> engine.executeStatement(
                "INSERT INTO mytable(pk, stringValue, longValue, nullableValue) VALUES (?, ?, ?, ?)",
                params,
                DatabaseTest.FAIL_ON_ERROR
        ));
    }

    private static TestEnvironment getTestEnvironment() {
        final ServiceLoader<TestEnvironment> loader = load(TestEnvironment.class);
        return StreamSupport.stream(loader.spliterator(), false)
                .findFirst()
                .orElse(new H2TestEnvironment());
    }

}
