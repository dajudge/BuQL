package com.dajudge.buql.reflector.predicate;

public interface ReflectorExpression {
    <T> T visit(ReflectorExpressionVisitor<T> visitor);
}
