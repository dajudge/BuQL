package com.dajudge.buql.test;

import com.dajudge.buql.query.dialect.Dialect;

import java.sql.Connection;
import java.sql.SQLException;

public interface TestEnvironment {
    Connection newConnection() throws SQLException;

    Dialect getDialect();
}
