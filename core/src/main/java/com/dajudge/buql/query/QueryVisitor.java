package com.dajudge.buql.query;

import com.dajudge.buql.query.model.expression.*;

public interface QueryVisitor<T> {
    T constantTrue();

    T dataCol(DataColExpression dataColExpression);

    T booleanOperation(BooleanOperation booleanOperation);

    T filterCol(FilterColExpression filterColExpression);

    T compare(QueryComparePredicate queryComparePredicate);

    T isNull(QueryExpression expression);
}
