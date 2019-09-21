package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.dialect.SqlCompareOperator;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

public class ComparePredicate extends BinaryPredicate {

    private final SqlCompareOperator operator;

    public ComparePredicate(final QueryExpression e0, final QueryExpression e1, final SqlCompareOperator operator) {
        super(e0, e1);
        this.operator = operator;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.compareOperator(sources, this, operator);
    }
}
