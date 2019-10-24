package com.dajudge.buql.query.model.select;

import com.dajudge.buql.query.model.QueryModel;
import com.dajudge.buql.query.model.expression.QueryPredicate;

import java.util.List;
import java.util.function.Function;

public class SelectQueryModel<Q> implements QueryModel<Q, SelectQuery> {
    private final QueryPredicate predicate;
    private final List<String> filterColumns;
    private final List<ProjectionColumn> projectionColumns;
    private final String tableName;
    private final Function<Q, List<Object>> filterValuesExtractor;

    public SelectQueryModel(
            final QueryPredicate predicate,
            final List<String> filterColumns,
            final List<ProjectionColumn> projectionColumns,
            final String tableName,
            final Function<Q, List<Object>> filterValuesExtractor
    ) {
        this.predicate = predicate;
        this.filterColumns = filterColumns;
        this.projectionColumns = projectionColumns;
        this.tableName = tableName;
        this.filterValuesExtractor = filterValuesExtractor;
    }

    @Override
    public SelectQuery createQuery(final Q queryObject) {
        return new SelectQuery(
                projectionColumns,
                filterValuesExtractor.apply(queryObject),
                filterColumns,
                predicate,
                tableName
        );
    }
}
