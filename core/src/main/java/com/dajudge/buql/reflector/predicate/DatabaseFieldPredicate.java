package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public class DatabaseFieldPredicate implements ReflectorPredicate {
    private final String fieldName;
    private final Function<Object, Object> read;
    private final ReflectorCompareOperator operator;

    public DatabaseFieldPredicate(
            final String fieldName,
            final Function<Object, Object> read,
            final ReflectorCompareOperator operator
    ) {
        this.fieldName = fieldName;
        this.read = read;
        this.operator = operator;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        final ReflectorExpression left = new DatabaseFieldExpression(fieldName);
        final ReflectorExpression right = new ParameterExpression(read);
        return visitor.compare(left, right, operator);
    }
}
