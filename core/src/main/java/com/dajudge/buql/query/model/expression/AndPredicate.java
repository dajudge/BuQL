package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

import java.util.List;

public class AndPredicate implements BooleanOperation {
    private final List<QueryPredicate> operands;

    public AndPredicate(final List<QueryPredicate> operands) {
        this.operands = operands;
    }

    @Override
    public String toSql(final ProjectionSources sources, final Dialect dialect) {
        return dialect.and(sources, this);
    }

    @Override
    public List<QueryPredicate> getOperands() {
        return operands;
    }
}
