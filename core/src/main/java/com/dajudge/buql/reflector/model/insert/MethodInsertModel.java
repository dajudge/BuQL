package com.dajudge.buql.reflector.model.insert;

import com.dajudge.buql.reflector.model.MethodModel;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MethodInsertModel<Q> extends MethodModel {
    private final Collection<String> insertFields;
    private final BiFunction<Q, String, Object> valueAccessor;
    private final Function<Object, List<Q>> preProcessor;

    public MethodInsertModel(
            final String tableName,
            final Collection<String> insertFields,
            final BiFunction<Q, String, Object> valueAccessor,
            final Function<Object, List<Q>> preProcessor
    ) {
        super(tableName);
        this.insertFields = insertFields;
        this.valueAccessor = valueAccessor;
        this.preProcessor = preProcessor;
    }

    public Collection<String> getInsertFields() {
        return insertFields;
    }

    public Object accessValue(final Q object, final String field) {
        return valueAccessor.apply(object, field);
    }

    public Function<Object, List<Q>> getPreProcessor() {
        return preProcessor;
    }
}
