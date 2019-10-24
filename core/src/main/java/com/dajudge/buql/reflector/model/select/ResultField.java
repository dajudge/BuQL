package com.dajudge.buql.reflector.model.select;

public class ResultField<R> {
    private final String tableColumn;
    private final String fieldName;

    public ResultField(
            final String tableColumn,
            final String fieldName
    ) {
        this.tableColumn = tableColumn;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getTableColumn() {
        return tableColumn;
    }
}
