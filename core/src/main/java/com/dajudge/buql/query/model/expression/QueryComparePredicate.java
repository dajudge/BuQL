package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

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
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.compare(this);
    }

    public QueryCompareOperator getOperator() {
        return operator;
    }
}
