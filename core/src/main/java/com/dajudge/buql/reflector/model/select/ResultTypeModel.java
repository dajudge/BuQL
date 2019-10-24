package com.dajudge.buql.reflector.model.select;

import java.util.List;
import java.util.function.Function;

public interface ResultTypeModel<R> {
    List<ResultField<R>> getResultFields();

    R newResultInstance(Function<String, Object> columnValues);
}
