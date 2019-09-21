package com.dajudge.buql.query.dialect;

import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.*;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;

import java.util.List;

import static java.util.stream.Collectors.joining;

public abstract class BaseDialect implements Dialect {

    @Override
    public QueryWithParameters select(final SelectQuery selectQuery) {
        return getSelectQueryWriter().toSql(selectQuery);
    }

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

    protected abstract SelectQueryWriter getSelectQueryWriter();
}
