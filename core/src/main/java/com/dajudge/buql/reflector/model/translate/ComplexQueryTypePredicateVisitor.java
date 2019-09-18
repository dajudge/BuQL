package com.dajudge.buql.reflector.model.translate;

import com.dajudge.buql.reflector.annotations.BooleanOperationType;
import com.dajudge.buql.reflector.model.visitor.OperandAccessor;
import com.dajudge.buql.reflector.model.visitor.QueryTypePredicateVisitor;
import com.dajudge.buql.reflector.predicate.BooleanOperationPredicate;
import com.dajudge.buql.reflector.predicate.EqualsPredicate;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class ComplexQueryTypePredicateVisitor implements QueryTypePredicateVisitor<ReflectorPredicate> {
    @Override
    public ReflectorPredicate booleanTypeOperator(
            final BooleanOperationType booleanOperator,
            final OperandAccessor operandAccessor
    ) {
        final List<ReflectorPredicate> operands = operandAccessor.getOperands().stream()
                .map(operand -> operand.visit(this))
                .collect(toList());
        switch (booleanOperator) {
            case OR:
                return BooleanOperationPredicate.or(operands);
            case AND:
                return BooleanOperationPredicate.and(operands);
            default:
                throw new IllegalArgumentException("Invalid boolean operator: " + booleanOperator);
        }
    }

    @Override
    public ReflectorPredicate equals(final String fieldName, final Function<Object, Object> read) {
        return new EqualsPredicate(fieldName, read);
    }
}
