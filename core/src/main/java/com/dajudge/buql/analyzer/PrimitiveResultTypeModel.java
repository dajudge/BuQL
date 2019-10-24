package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.model.select.ResultField;
import com.dajudge.buql.reflector.model.select.ResultTypeModel;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;

public class PrimitiveResultTypeModel<R> implements ResultTypeModel<R> {

    private final String fieldName;

    private PrimitiveResultTypeModel(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public List<ResultField<R>> getResultFields() {
        return singletonList(new ResultField<>(fieldName, fieldName));
    }


    @Override
    public R newResultInstance(final Function<String, Object> columnValues) {
        return (R) columnValues.apply(fieldName);
    }

    public static <R> ResultTypeModel<R> create(final Class<R> clazz, final String fieldName) {
        return new PrimitiveResultTypeModel<>(fieldName);
    }
}
