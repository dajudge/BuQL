package com.dajudge.buql.query.dialect.base;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.SqlCompareOperator;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseDialect implements Dialect {
    @Override
    public String booleanOperation(final List<String> operands, final String operator) {
        return operands.size() == 1 ?
                operands.get(0) :
                operands.stream().collect(Collectors.joining(" " + operator + " "));
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
            final String left,
            final String right,
            final SqlCompareOperator op
    ) {
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
