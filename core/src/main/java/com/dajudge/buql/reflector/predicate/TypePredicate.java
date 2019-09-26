package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.reflector.annotations.BooleanOperator;

import java.util.List;
import java.util.function.Function;

import static com.dajudge.buql.reflector.predicate.ReflectorBooleanOperator.AND;

public class TypePredicate implements ReflectorPredicate {
    private final Class<?> type;
    private final Function<Object, Object> parentAccessor;

    public TypePredicate(final Class<?> type, final Function<Object, Object> parentAccessor) {
        this.type = type;
        this.parentAccessor = parentAccessor;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        final BooleanOperator booleanOperator = type.getAnnotation(BooleanOperator.class);
        if (booleanOperator != null) {
            final List<ReflectorPredicate> operands = new ExplicitTypeReflector(type, parentAccessor).getOperands();
            return visitor.booleanOperation(new BooleanOperationPredicate(operatorOf(booleanOperator), operands));
        }
        return visitor.booleanOperation(
                new BooleanOperationPredicate(AND, new ImplicitTypeReflector(type, parentAccessor).getOperands())
        );
    }

    private ReflectorBooleanOperator operatorOf(final BooleanOperator booleanOperator) {
        switch (booleanOperator.value()) {
            case AND:
                return AND;
            case OR:
                return ReflectorBooleanOperator.OR;
            default:
                throw new IllegalArgumentException("Unknown boolean operator: " + booleanOperator.value());
        }
    }

}
