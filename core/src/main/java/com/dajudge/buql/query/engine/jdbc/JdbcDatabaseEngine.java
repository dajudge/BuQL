package com.dajudge.buql.query.engine.jdbc;

import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DatabaseResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JdbcDatabaseEngine implements DatabaseEngine {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcDatabaseEngine.class);
    private final Consumer<Consumer<Connection>> caller;

    public JdbcDatabaseEngine(final Consumer<Consumer<Connection>> caller) {
        this.caller = caller;
    }

    @Override
    public void executeQuery(
            final String query,
            final List<Object> params,
            final DatabaseResultCallback cb
    ) {
        caller.accept(c -> executeQuery(c, query, params, cb));
    }

    private void executeQuery(
            final Connection connection,
            final String sql,
            final List<? extends Object> params,
            final DatabaseResultCallback cb
    ) {
        final JdbcConsumer<PreparedStatement> consumer = s -> {
            try (final ResultSet rs = s.executeQuery()) {
                cb.onMetadata(metadataOf(rs.getMetaData()));
                while (rs.next()) {
                    cb.onRow(rowOf(rs));
                }
            }
        };
        executeStatement(connection, sql, params, consumer, cb);
    }

    private void executeStatement(
            final Connection connection,
            final String sql,
            final List<? extends Object> params,
            final JdbcConsumer<PreparedStatement> consumer,
            final DatabaseResultCallback cb
    ) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Preparing statement: {} {}", sql, params);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("Preparing statement: {} {}", sql, params);
        }
        try (final PreparedStatement s = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                s.setObject(i + 1, params.get(i));
            }
            consumer.apply(s);
        } catch (final WrappedSqlException e) {
            cb.onError(e.getCause());
        } catch (final SQLException e) {
            cb.onError(e);
        } finally {
            cb.done();
        }
    }

    private DatabaseResultCallback.ResultRow rowOf(final ResultSet rs) {
        return new DatabaseResultCallback.ResultRow() {
            @Override
            public Object getObject(final String colName) {
                return safe(() -> rs.getObject(colName));
            }
        };
    }

    private DatabaseResultCallback.ResultMetadata metadataOf(final ResultSetMetaData rs) {
        return new DatabaseResultCallback.ResultMetadata() {
            @Override
            public List<String> getColumnNames() {
                return safe(() -> {
                    final List<String> columnNames = new ArrayList<>();
                    for (int i = 0; i < rs.getColumnCount(); i++) {
                        columnNames.add(rs.getColumnLabel(i + 1));
                    }
                    return columnNames;
                });
            }
        };
    }

    private static <O> O safe(final JdbcSupplier<O> f) {
        try {
            return f.get();
        } catch (final SQLException e) {
            throw new WrappedSqlException(e);
        }
    }

    @Override
    public void executeStatement(
            final String sql,
            final List<? extends Object> params,
            final DatabaseResultCallback cb
    ) {
        caller.accept(c -> {
            final JdbcConsumer<PreparedStatement> consumer = s -> cb.onUpdate(s.executeUpdate());
            executeStatement(c, sql, params, consumer, cb);
        });
    }

    interface JdbcSupplier<O> {
        O get() throws SQLException;
    }

    interface JdbcConsumer<I> {
        void apply(I i) throws SQLException;
    }

    static class WrappedSqlException extends RuntimeException {
        WrappedSqlException(final SQLException e) {
            super(e);
        }

        @Override
        public synchronized SQLException getCause() {
            return (SQLException) super.getCause();
        }
    }
}
