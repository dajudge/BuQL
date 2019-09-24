package com.dajudge.buql.test.shared;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import com.dajudge.buql.query.engine.jdbc.JdbcDatabaseEngine;
import com.dajudge.buql.test.TestEnvironment;
import com.dajudge.buql.test.shared.h2.H2TestEnvironment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.ServiceLoader.load;

public abstract class DatabaseTest {
    public static final DefaultDatabaseResultCallback FAIL_ON_ERROR = new DefaultDatabaseResultCallback() {
        @Override
        public void onError(final SQLException e) {
            throw new RuntimeException("Failed to execute setup SQL", e);
        }
    };

    private Connection testInstanceConnection;
    protected JdbcDatabaseEngine engine;

    @Before
    public void setupDatabase() throws SQLException {
        testInstanceConnection = TestContainer.newConnection();
        testInstanceConnection.setAutoCommit(false);
        engine = new JdbcDatabaseEngine(connectionConsumer -> connectionConsumer.accept(testInstanceConnection));
    }

    @After
    public void teardown() throws SQLException {
        testInstanceConnection.close();
    }

}
