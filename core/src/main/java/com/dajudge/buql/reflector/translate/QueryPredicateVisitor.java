package com.dajudge.buql.reflector.translate;

import com.dajudge.buql.query.model.expression.QueryPredicate;
import com.dajudge.buql.reflector.predicate.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QueryPredicateVisitor implements ReflectorPredicateVisitor<QueryPredicate> {
    private final FilterFieldsMapping filterFieldsMapping;

    public QueryPredicateVisitor(final FilterFieldsMapping filterFieldsMapping) {
        this.filterFieldsMapping = filterFieldsMapping;
    }

    @Override
    public QueryPredicate booleanOperation(final BooleanOperationPredicate predicate) {
        return QueryPredicate.or(operandsOf(predicate));
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
        final List<ReflectorPredicate> operands = predicate.getOperands();
        return operands.stream()
                .map(op -> op.visit(new QueryPredicateVisitor(filterFieldsMapping)))
                .collect(toList());
    }
}
