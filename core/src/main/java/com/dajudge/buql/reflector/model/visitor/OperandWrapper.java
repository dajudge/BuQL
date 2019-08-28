package com.dajudge.buql.reflector.model.visitor;

public interface OperandWrapper {
    <T> T visit(QueryTypePredicateVisitor<T> visitor);
}
