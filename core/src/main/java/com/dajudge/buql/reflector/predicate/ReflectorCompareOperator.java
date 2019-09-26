package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.query.model.expression.QueryCompareOperator;

public interface ReflectorCompareOperator {
    ReflectorCompareOperator LIKE = () -> QueryCompareOperator.LIKE;
    ReflectorCompareOperator EQUALS = () -> QueryCompareOperator.EQUALS;
    ReflectorCompareOperator NOT_EQUALS = () -> QueryCompareOperator.NOT_EQUALS;
    ReflectorCompareOperator GTE = () -> QueryCompareOperator.GTE;
    ReflectorCompareOperator GT = () -> QueryCompareOperator.GT;
    ReflectorCompareOperator LT = () -> QueryCompareOperator.LT;
    ReflectorCompareOperator LTE = () -> QueryCompareOperator.LTE;

    QueryCompareOperator getOperator();
}
