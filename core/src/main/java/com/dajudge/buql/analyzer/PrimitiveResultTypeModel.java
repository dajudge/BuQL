package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.model.MethodSelectModelFactory;
import com.dajudge.buql.reflector.model.ResultField;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;

public class PrimitiveResultTypeModel<R> implements MethodSelectModelFactory.ResultTypeModel<R> {

    private final String fieldName;

    public PrimitiveResultTypeModel(final String fieldName) {
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
}
