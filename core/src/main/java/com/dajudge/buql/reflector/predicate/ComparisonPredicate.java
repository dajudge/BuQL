package com.dajudge.buql.reflector.predicate;

public class ComparisonPredicate implements ReflectorPredicate {
    private final ReflectorCompareOperator operator;
    private final ReflectorExpression left;
    private final ParameterExpression right;

    public ComparisonPredicate(
            final ReflectorCompareOperator operator,
            final ReflectorExpression left,
            final ParameterExpression right
    ) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        return visitor.compare(left, right, operator);
    }
}
