package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.postgres.PostgresDialect;
import com.dajudge.buql.test.TestEnvironment;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static java.lang.String.format;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

public class PostgresTestEnvironment implements TestEnvironment {
    private static final class PostgresManager {
        private final String defaultUrl;
        private final EmbeddedPostgres postgres;

        public PostgresManager() {
            try {
                postgres = new EmbeddedPostgres(V9_6);
                defaultUrl = postgres.start();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to start embedded postgres", e);
            }
            Runtime.getRuntime().addShutdownHook(new Thread(postgres::stop));
        }

        public Connection newConnection() throws SQLException {
            final String dbName = ("testdb_" + UUID.randomUUID().toString()).replace("-", "");
            try (
                    final Connection c = DriverManager.getConnection(defaultUrl);
                    final PreparedStatement s = c.prepareStatement("CREATE DATABASE " + dbName)
            ) {
                s.execute();
            }
            final PostgresConfig config = postgres.getConfig().get();
            final String jdbcUrl = format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
                    config.net().host(),
                    config.net().port(),
                    dbName,
                    config.credentials().username(),
                    config.credentials().password()
            );
            return DriverManager.getConnection(jdbcUrl);
        }
    }

    private static PostgresManager manager;

    @Override
    public Connection newConnection() throws SQLException {
        if (manager == null) {
            manager = new PostgresManager();
        }
        return manager.newConnection();
    }

    @Override
    public Dialect getDialect() {
        return new PostgresDialect();
    }
}
