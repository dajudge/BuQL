package com.dajudge.buql.reflector.model.insert;

import com.dajudge.buql.query.model.insert.InsertQueryModel;
import com.dajudge.buql.reflector.ReflectInsertQuery;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class InsertMethodModelTranslator {
    private InsertMethodModelTranslator() {
    }

    public static <Q> ReflectInsertQuery<Q> translateMethodModelToQuery(final MethodInsertModel<Q> model) {
        final Collection<String> insertFields = model.getInsertFields();
        final Function<Q, Map<String, Object>> insertValuesExtractor = o -> {
            final Map<String, Object> ret = new HashMap<>();
            insertFields.forEach(it -> ret.put(it, model.accessValue(o, it)));
            return ret;
        };
        final InsertQueryModel<Q> queryModel = new InsertQueryModel<>(model.getTableName(), insertValuesExtractor);
        final Function<Object, List<Q>> preProcessor = list -> (List<Q>) list;
        return new ReflectInsertQuery<>(queryModel, preProcessor);
    }
}
