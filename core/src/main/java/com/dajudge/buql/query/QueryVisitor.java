package com.dajudge.buql.query;

import com.dajudge.buql.query.model.expression.BooleanOperation;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.expression.QueryComparePredicate;

public interface QueryVisitor<T> {
    T constantTrue();

    T dataCol(DataColExpression dataColExpression);

    T booleanOperation(BooleanOperation booleanOperation);

    T filterCol(FilterColExpression filterColExpression);

    T compare(QueryComparePredicate queryComparePredicate);
}
