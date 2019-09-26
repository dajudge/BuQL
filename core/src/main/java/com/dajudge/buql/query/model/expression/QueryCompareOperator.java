package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.SqlCompareOperator;

public interface QueryCompareOperator {
    QueryCompareOperator EQUALS = () -> SqlCompareOperator.EQUALS;
    QueryCompareOperator LIKE = () -> SqlCompareOperator.LIKE;
    QueryCompareOperator GTE = () -> SqlCompareOperator.GTE;
    QueryCompareOperator GT = () -> SqlCompareOperator.GT;
    QueryCompareOperator LT = () -> SqlCompareOperator.LT;
    QueryCompareOperator LTE = () -> SqlCompareOperator.LTE;

    SqlCompareOperator getOperator();
}
