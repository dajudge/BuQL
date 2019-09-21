package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.query.model.expression.QueryCompareOperator;

public interface ReflectorCompareOperator {
    ReflectorCompareOperator LIKE = () -> QueryCompareOperator.LIKE;
    ReflectorCompareOperator EQUALS = () -> QueryCompareOperator.EQUALS;

    QueryCompareOperator getOperator();
}
