package com.dajudge.buql.reflector.model;

public class MethodModel {
    private final String tableName;

    public MethodModel(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
