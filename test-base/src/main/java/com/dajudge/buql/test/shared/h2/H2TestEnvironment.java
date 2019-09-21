package com.dajudge.buql.test.shared.h2;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.h2.H2Dialect;
import com.dajudge.buql.test.TestEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.util.UUID.randomUUID;

public class H2TestEnvironment implements TestEnvironment {
    @Override
    public Connection newConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:" + randomUUID().toString() + ";MODE=PostgreSQL");
    }

    @Override
    public Dialect getDialect() {
        return new H2Dialect();
    }
}
