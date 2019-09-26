package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.reflector.annotations.BooleanOperationType;

import java.util.List;

public class OrPredicate extends BooleanOperation {

    public OrPredicate(final List<QueryPredicate> operands) {
        super(BooleanOperationType.OR, operands);
    }
}
