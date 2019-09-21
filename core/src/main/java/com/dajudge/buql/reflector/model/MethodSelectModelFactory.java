package com.dajudge.buql.reflector.model;

import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MethodSelectModelFactory {

    public interface ResultTypeModel<R> {
        List<ResultField<R>> getResultFields();
        R newResultInstance(Function<String, Object> columnValues);
    }

    public static <Q, R> MethodSelectModel<Q, R> createSelectModel(
            final String tableName,
            final ReflectorPredicate predicate,
            final ResultTypeModel<R> resultTypeModel,
            final Function<Object, Map<String, Q>> preProcessor,
            final Function<Map<String, List<R>>, ?> postProcessor
    ) {
        final List<ResultField<R>> resultFields = resultTypeModel.getResultFields();
        final Function<Function<String, Object>, R> resultTypeFactory = resultTypeModel::newResultInstance;
        return new MethodSelectModel<>(
                predicate,
                tableName,
                resultFields,
                resultTypeFactory,
                preProcessor,
                postProcessor
        );
    }

}
