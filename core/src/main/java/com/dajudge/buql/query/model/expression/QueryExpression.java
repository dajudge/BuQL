package com.dajudge.buql.query.model.expression;

import com.dajudge.buql.query.QueryVisitor;

public interface QueryExpression {
    <T> T visit(QueryVisitor<T> visitor);
}
