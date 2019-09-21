package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class QueryComparePredicate extends BinaryPredicate {

    private final QueryCompareOperator operator;

    public QueryComparePredicate(
            final QueryExpression e0,
            final QueryExpression e1,
            final QueryCompareOperator operator
    ) {
        super(e0, e1);
        this.operator = operator;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.compareOperator(sources, this, operator.getOperator());
    }
}
