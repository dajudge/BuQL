package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.SqlCompareOperator;

public interface QueryCompareOperator {
    QueryCompareOperator EQUALS = () -> SqlCompareOperator.EQUALS;
    QueryCompareOperator LIKE = () -> SqlCompareOperator.LIKE;
    QueryCompareOperator GTE = () -> SqlCompareOperator.GTE;
    QueryCompareOperator GT = () -> SqlCompareOperator.GT;

    SqlCompareOperator getOperator();
}
