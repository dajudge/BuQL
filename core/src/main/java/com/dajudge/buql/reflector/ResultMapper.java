package com.dajudge.buql.reflector;

import java.util.function.Function;

public class ResultMapper<R> {
    private final Function<String, String> colNameMapper;
    private final Function<Function<String, Object>, R> resultInstanceFactory;
    private final String idCol;

    public ResultMapper(
            final Function<String, String> colNameMapper,
            final Function<Function<String, Object>, R> resultInstanceFactory,
            final String idCol
    ) {
        this.colNameMapper = colNameMapper;
        this.resultInstanceFactory = resultInstanceFactory;
        this.idCol = idCol;
    }

    public String getId(final Function<String, Object> colAccessor) {
        return colAccessor.apply(idCol).toString();
    }

    public R getResultObject(final Function<String, Object> colAccessor) {
        return resultInstanceFactory.apply(colNameMapper.andThen(colAccessor));
    }
}
