package com.dajudge.buql.query.engine;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseResultCallback {
    void onMetadata(ResultMetadata metadata);

    void onRow(ResultRow row);

    void onError(SQLException e);

    void done();

    void onUpdate(int updatedRowCoun);

    interface ResultMetadata {
        List<String> getColumnNames();
    }

    interface ResultRow {
        Object getObject(String colName);
    }
}
