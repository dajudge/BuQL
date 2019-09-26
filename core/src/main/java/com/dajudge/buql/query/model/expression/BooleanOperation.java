package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;
import com.dajudge.buql.reflector.annotations.BooleanOperationType;

import java.util.List;

public class BooleanOperation implements QueryPredicate {
    private final BooleanOperationType op;
    private final List<QueryPredicate> operands;

    public BooleanOperation(final BooleanOperationType op, List<QueryPredicate> operands) {
        this.op = op;
        this.operands = operands;
    }

    @Override
    public <T> T visit(final QueryVisitor<T> visitor) {
        return visitor.booleanOperation(this);
    }

    public BooleanOperationType getOperator() {
        return op;
    }

    public List<QueryPredicate> getOperands() {
        return operands;
    }
}
