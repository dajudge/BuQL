package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.mariadb.MariadbDialect;
import com.dajudge.buql.query.dialect.postgres.PostgresDialect;
import com.dajudge.buql.test.TestEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.util.UUID.randomUUID;

public class MariadbTestEnvironment implements TestEnvironment {
    @Override
    public Connection newConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:tc:mariadb:10.4.8://somehostname:someport/testdb");
    }

    @Override
    public Dialect getDialect() {
        return new MariadbDialect();
    }
}
