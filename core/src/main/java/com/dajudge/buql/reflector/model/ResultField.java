package com.dajudge.buql.reflector.model;

import java.util.function.BiConsumer;

public class ResultField<R> {
    private final String tableColumn;
    private final BiConsumer<R, Object> setter;

    public ResultField(
            final String tableColumn,
            final BiConsumer<R, Object> setter
    ) {
        this.tableColumn = tableColumn;
        this.setter = setter;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void set(final R object, final Object value) {
        setter.accept(object, value);
    }
}
