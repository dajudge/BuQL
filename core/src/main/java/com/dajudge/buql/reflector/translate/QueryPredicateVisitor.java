package com.dajudge.buql.reflector.translate;

import com.dajudge.buql.query.model.expression.QueryPredicate;
import com.dajudge.buql.reflector.predicate.BooleanOperationPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;
import com.dajudge.buql.reflector.predicate.ReflectorExpression;
import com.dajudge.buql.reflector.predicate.ReflectorPredicateVisitor;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QueryPredicateVisitor implements ReflectorPredicateVisitor<QueryPredicate> {
    private final FilterFieldsMapping filterFieldsMapping;

    public QueryPredicateVisitor(final FilterFieldsMapping filterFieldsMapping) {
        this.filterFieldsMapping = filterFieldsMapping;
    }

    @Override
    public QueryPredicate or(final BooleanOperationPredicate predicate) {
        return QueryPredicate.or(operandsOf(predicate));
    }

    @Override
    public QueryPredicate and(final BooleanOperationPredicate predicate) {
        return QueryPredicate.and(operandsOf(predicate));
    }

    @Override
    public QueryPredicate compare(
            final ReflectorExpression left,
            final ReflectorExpression right,
            final ReflectorCompareOperator operator
    ) {
        return QueryPredicate.compare(
                left.visit(new QueryExpressionVisitor(filterFieldsMapping)),
                right.visit(new QueryExpressionVisitor(filterFieldsMapping)),
                operator.getOperator()
        );
    }

    private List<QueryPredicate> operandsOf(final BooleanOperationPredicate predicate) {
        return predicate.getOperands().stream()
                .map(op -> op.visit(new QueryPredicateVisitor(filterFieldsMapping)))
                .collect(toList());
    }
}
