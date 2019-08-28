package com.dajudge.buql.query.model.expression;

import java.util.List;

public interface BooleanOperation extends QueryPredicate {
    List<QueryPredicate> getOperands();
}
