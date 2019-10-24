package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.reflector.model.select.ResultTypeModel;

public interface ResultTypeToPredicateFactory {
    <R> ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final String resultFieldName
    );
}
