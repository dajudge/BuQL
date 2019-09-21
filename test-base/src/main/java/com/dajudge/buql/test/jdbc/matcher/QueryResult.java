package com.dajudge.buql.test.jdbc.matcher;

import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.engine.DefaultDatabaseResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class QueryResult {
    private static final Logger LOG = LoggerFactory.getLogger(QueryResult.class);
    private SQLException exception;
    private List<String> columnNames;
    private List<Map<String, Object>> rows = new ArrayList<>();
    private boolean done;

    public static DatabaseResultCallback into(final QueryResult queryResult) {
        return new DefaultDatabaseResultCallback() {
            @Override
            public void onMetadata(final ResultMetadata metadata) {
                queryResult.setColumns(metadata.getColumnNames());
            }

            @Override
            public void onRow(final ResultRow row) {
                final Map<String, Object> mappedRow = queryResult.getColumnNames().stream()
                        .collect(toMap(
                                c -> c,
                                c -> row.getObject(c)
                        ));
                LOG.info("ROW: {}", mappedRow);
                queryResult.addRow(mappedRow);
            }

            @Override
            public void onError(final SQLException e) {
                queryResult.setException(e);
            }

            @Override
            public void done() {
                queryResult.setDone();

            }
        };
    }

    public void setColumns(final List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void addRow(final Map<String, Object> row) {
        rows.add(row);
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(final SQLException exception) {
        this.exception = exception;
    }

    public void setDone() {
        done = true;
    }
}
