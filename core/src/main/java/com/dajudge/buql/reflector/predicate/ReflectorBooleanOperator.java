package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.query.model.expression.QueryPredicate;

import java.util.List;

public interface ReflectorBooleanOperator {
    ReflectorBooleanOperator AND = QueryPredicate::and;
    ReflectorBooleanOperator OR = QueryPredicate::or;

    QueryPredicate createQueryPredicate(List<QueryPredicate> operands);
}
