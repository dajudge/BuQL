package com.dajudge.buql.reflector.model;

import com.dajudge.buql.query.model.SelectQueryModel;
import com.dajudge.buql.query.model.expression.QueryPredicate;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.ResultMapper;
import com.dajudge.buql.reflector.translate.FilterFieldsMapping;
import com.dajudge.buql.reflector.translate.QueryPredicateVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.dajudge.buql.query.model.select.ProjectionColumn.fromData;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class MethodModelTranslator {
    private static final String ROW_ID_COLUMN = "M_ID";
    private static final String ROW_ID_LABEL = "P_ID";

    private MethodModelTranslator() {
    }

    private static class ProjectionColumnWithSetter<R> {

        private final ProjectionColumn projectionColumn;
        private final BiConsumer<R, Object> setter;
        private final String label;

        ProjectionColumnWithSetter(
                final String label,
                final ProjectionColumn projectionColumn,
                final BiConsumer<R, Object> setter
        ) {
            this.label = label;
            this.projectionColumn = projectionColumn;
            this.setter = setter;
        }
    }

    public static <Q, R> ReflectSelectQuery<Q, R> translateMethodModelToQuery(final MethodSelectModel<Q, R> model) {
        final String tableName = model.getTableName();
        final FilterFieldsMapping filterFieldsMapping = new FilterFieldsMapping();
        final QueryPredicate predicate = model.getPredicate().visit(new QueryPredicateVisitor(filterFieldsMapping));
        final List<String> filterColumns = getFilterColumns(filterFieldsMapping);
        final List<ProjectionColumnWithSetter<R>> projectionColumns = getProjectionColumns(model);
        final Function<Map<String, Q>, List<Object>> extractor = getFilterValuesExtractor(filterColumns, filterFieldsMapping);
        final SelectQueryModel<Map<String, Q>> queryModel = new SelectQueryModel<>(
                predicate,
                filterColumns,
                projectionColumns.stream().map(it -> it.projectionColumn).collect(toList()),
                tableName,
                extractor
        );
        final Supplier<R> factory = model.getResultFactory();
        final Map<String, BiConsumer<R, Object>> setters = projectionColumns.stream()
                .collect(toMap(it -> it.label, it -> it.setter));
        final ResultMapper<R> resultMapper = new ResultMapper<>(factory, setters, ROW_ID_LABEL);
        return new ReflectSelectQuery<>(queryModel, resultMapper);
    }

    private static List<String> getFilterColumns(final FilterFieldsMapping filterFieldsMapping) {
        return Stream.concat(
                Stream.of(ROW_ID_COLUMN),
                filterFieldsMapping.getFilterColumns().stream()
        ).collect(toList());
    }

    private static <Q, R> List<ProjectionColumnWithSetter<R>> getProjectionColumns(final MethodSelectModel<Q, R> model) {
        final AtomicInteger counter = new AtomicInteger();
        final List<ResultField<R>> resultFields = model.getResultFields();
        final Stream<ProjectionColumnWithSetter<R>> resultProjectionColumns = resultFields.stream()
                .map(field -> {
                    final String label = "R" + counter.getAndIncrement();
                    return new ProjectionColumnWithSetter<>(
                            label,
                            fromData(field.getTableColumn(), label),
                            field::set
                    );
                });
        return Stream.concat(
                Stream.of(new ProjectionColumnWithSetter<R>(
                        ROW_ID_LABEL,
                        ProjectionColumn.fromFilters(ROW_ID_COLUMN, ROW_ID_LABEL),
                        (a, o) -> {
                        }
                )),
                resultProjectionColumns
        ).collect(toList());
    }

    private static <Q> Function<Map<String, Q>, List<Object>> getFilterValuesExtractor(
            final List<String> filterColumns,
            final FilterFieldsMapping filterFieldsMapping
    ) {
        return rows -> {
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("Empty query object map");
            }
            return rows.entrySet().stream()
                    .map(row -> getRowValues(row, filterColumns, filterFieldsMapping))
                    .flatMap(Collection::stream)
                    .collect(toList());
        };
    }

    private static <Q> List<Object> getRowValues(
            final Map.Entry<String, Q> row,
            final List<String> filterColumns,
            final FilterFieldsMapping filterFieldsMapping
    ) {
        final List<Object> ret = new ArrayList<>();
        ret.add(row.getKey());
        final Map<String, Object> mappedRow = filterFieldsMapping.extractFilterFields(row.getValue());
        for (int i = 1; i < filterColumns.size(); i++) {
            ret.add(mappedRow.get(filterColumns.get(i)));
        }
        return ret;
    }
}
