package com.dajudge.buql.query.model.insert;

import com.dajudge.buql.query.model.QueryModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class InsertQueryModel<Q> implements QueryModel<List<Q>, InsertQuery> {
    private final String table;
    private final Function<Q, Map<String, Object>> insertValuesExtractor;

    public InsertQueryModel(final String table, Function<Q, Map<String, Object>> insertValuesExtractor) {
        this.table = table;
        this.insertValuesExtractor = insertValuesExtractor;
    }

    @Override
    public InsertQuery createQuery(final List<Q> queryObject) {
        return new InsertQuery(table, queryObject.stream()
                .map(insertValuesExtractor)
                .collect(toList()));
    }
}
