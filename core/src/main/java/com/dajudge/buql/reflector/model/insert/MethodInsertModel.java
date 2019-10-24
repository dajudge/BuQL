package com.dajudge.buql.reflector.model.insert;

import com.dajudge.buql.reflector.model.MethodModel;

import java.util.Collection;
import java.util.function.BiFunction;

public class MethodInsertModel<Q> extends MethodModel {
    private final Collection<String> insertFields;
    private final BiFunction<Q, String, Object> valueAccessor;

    public MethodInsertModel(
            final String tableName,
            final Collection<String> insertFields,
            final BiFunction<Q, String, Object> valueAccessor
    ) {
        super(tableName);
        this.insertFields = insertFields;
        this.valueAccessor = valueAccessor;
    }

    public Collection<String> getInsertFields() {
        return insertFields;
    }

    public Object accessValue(final Q object, final String field) {
        return valueAccessor.apply(object, field);
    }
}
