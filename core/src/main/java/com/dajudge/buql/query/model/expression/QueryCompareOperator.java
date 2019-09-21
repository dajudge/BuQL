package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.dialect.SqlCompareOperator;

public interface QueryCompareOperator {
    QueryCompareOperator EQUALS = () -> SqlCompareOperator.EQUALS;
    QueryCompareOperator LIKE = () -> SqlCompareOperator.LIKE;

    SqlCompareOperator getOperator();
}
