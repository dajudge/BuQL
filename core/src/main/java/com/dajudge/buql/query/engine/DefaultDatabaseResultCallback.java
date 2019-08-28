package com.dajudge.buql.query.engine;

import java.sql.SQLException;

public class DefaultDatabaseResultCallback implements DatabaseResultCallback {
    @Override
    public void onMetadata(final ResultMetadata metadata) {

    }

    @Override
    public void onRow(final ResultRow row) {

    }

    @Override
    public void onError(final SQLException e) {

    }

    @Override
    public void done() {

    }

    @Override
    public void onUpdate(final int updatedRowCoun) {

    }
}
