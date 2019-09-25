package com.dajudge.buql.query.dialect.base;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.SqlCompareOperator;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.*;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

public abstract class BaseDialect implements Dialect {

    @Override
    public String and(final ProjectionSources sources, final AndPredicate predicate) {
        return booleanOperator(sources, predicate, "AND");
    }

    @Override
    public String or(final ProjectionSources sources, final OrPredicate predicate) {
        return booleanOperator(sources, predicate, "OR");
    }

    private String booleanOperator(final ProjectionSources sources, final BooleanOperation predicate, final String op) {
        final List<QueryPredicate> operands = predicate.getOperands();
        if (operands.isEmpty()) {
            throw new IllegalArgumentException("No operands for " + op + " operator");
        }
        if (operands.size() == 1) {
            return operands.get(0).toSql(sources, this);
        }
        return "(" + operands.stream().map(it -> it.toSql(sources, this)).collect(joining(op)) + ")";
    }

    @Override
    public String dataColumn(final ProjectionSources sources, final DataColExpression dataColExpression) {
        return sources.getDataColumn(dataColExpression.getColumnName());
    }

    @Override
    public String filterColumn(final ProjectionSources sources, final FilterColExpression filterColExpression) {
        return sources.getFiltersColumn(filterColExpression.getColumnName());
    }

    @Override
    public String constTrue() {
        return "TRUE";
    }

    @Override
    public String compareOperator(
            final ProjectionSources sources,
            final BinaryPredicate predicate,
            final SqlCompareOperator op
    ) {
        final String left = predicate.getLeft().toSql(sources, this);
        final String right = predicate.getRight().toSql(sources, this);
        return op.toSql(left, right);
    }


    @Override
    public final List<QueryWithParameters> select(final SelectQuery selectQuery) {
        return partitionQuery(selectQuery, this::selectInternal);
    }

    protected abstract QueryWithParameters selectInternal(final SelectQuery selectQuery);

    protected abstract int getMaxParamsPerQuery();

    private List<QueryWithParameters> partitionQuery(
            final SelectQuery originalQuery,
            final Function<SelectQuery, QueryWithParameters> translator
    ) {
        final int maxParamsPerQuery = getMaxParamsPerQuery();
        final int paramsPerFilterRow = originalQuery.getParamsPerFilterRow();
        if (paramsPerFilterRow > maxParamsPerQuery) {
            throw new IllegalArgumentException(
                    "Cannot process query with more than " + maxParamsPerQuery + " params per query row"
            );
        }
        final int rowsPerQuery = maxParamsPerQuery / paramsPerFilterRow;
        final int paramsPerQuery = rowsPerQuery * paramsPerFilterRow;
        final int fullQueries = originalQuery.getFilterRowCount() / rowsPerQuery;
        final boolean hasOverhangQuery = originalQuery.getFilterRowCount() % rowsPerQuery > 0;
        final int allQueries = fullQueries + (hasOverhangQuery ? 1 : 0);
        final List<QueryWithParameters> ret = new ArrayList<>();
        for (int i = 0; i < allQueries; i++) {
            final int currentStartIndex = i * paramsPerQuery;
            final int currentEndIndex = currentStartIndex + paramsPerQuery;
            final List<Object> currentParams = originalQuery
                    .getFilterParameters()
                    .subList(currentStartIndex, Math.min(currentEndIndex, originalQuery.getFilterParameters().size()));
            final SelectQuery currentQuery = new SelectQuery(
                    originalQuery.getProjectionColumns(),
                    currentParams,
                    originalQuery.getFilterColumns(),
                    originalQuery.getPredicate(),
                    originalQuery.getQueryTable()
            );
            ret.add(translator.apply(currentQuery));
        }
        return ret;
    }
}
