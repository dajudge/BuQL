package com.dajudge.buql.reflector.predicate;

public class IsNullPredicate implements ReflectorPredicate {
    private final ReflectorExpression expression;

    public IsNullPredicate(final ReflectorExpression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.isNull(expression);
    }
}
