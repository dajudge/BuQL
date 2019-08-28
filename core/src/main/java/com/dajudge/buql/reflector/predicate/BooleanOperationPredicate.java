package com.dajudge.buql.reflector.predicate;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public abstract class BooleanOperationPredicate implements ReflectorPredicate {
    public static BooleanOperationPredicate and(final List<ReflectorPredicate> operands) {
        return new BooleanOperationPredicate(operands) {
            @Override
            public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
                return visitor.and(this);
            }
        };
    }

    public static BooleanOperationPredicate or(final List<ReflectorPredicate> operands) {
        return new BooleanOperationPredicate(operands) {
            @Override
            public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
                return visitor.or(this);
            }
        };
    }

    private final List<ReflectorPredicate> operands;

    private BooleanOperationPredicate(
            final List<ReflectorPredicate> operands
    ) {
        this.operands = unmodifiableList(operands);
    }

    @Override
    public abstract <T> T visit(final ReflectorPredicateVisitor<T> visitor);

    public List<ReflectorPredicate> getOperands() {
        return operands;
    }
}
