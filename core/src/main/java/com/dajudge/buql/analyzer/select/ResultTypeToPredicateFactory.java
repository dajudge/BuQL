package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.reflector.model.MethodSelectModelFactory;

public interface ResultTypeToPredicateFactory {
    <R> MethodSelectModelFactory.ResultTypeModel<R> createResultFieldsModel(
            final Class<R> clazz,
            final String resultFieldName
    );
}
