package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.query.model.expression.QueryPredicate;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class BooleanOperationPredicate implements ReflectorPredicate {
    private final ReflectorBooleanOperator operator;
    private final List<ReflectorPredicate> operands;

    public BooleanOperationPredicate(
            final ReflectorBooleanOperator operator,
            final List<ReflectorPredicate> operands
    ) {
        this.operator = operator;
        this.operands = unmodifiableList(operands);
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.booleanOperation(this);
    }

    public List<ReflectorPredicate> getOperands() {
        return operands;
    }

    public ReflectorBooleanOperator getOperator() {
        return operator;
    }
}
