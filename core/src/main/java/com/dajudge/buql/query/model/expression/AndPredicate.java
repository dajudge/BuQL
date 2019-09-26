package com.dajudge.buql.query.model.expression;

import java.util.List;

import static com.dajudge.buql.reflector.annotations.BooleanOperationType.AND;

public class AndPredicate extends BooleanOperation {
    public AndPredicate(final List<QueryPredicate> operands) {
        super(AND, operands);
    }
}
