package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public abstract class DatabaseFieldPredicate implements ReflectorPredicate {
    private final String fieldName;
    private final Function<Object, Object> read;

    public DatabaseFieldPredicate(final String fieldName, final Function<Object, Object> read) {
        this.fieldName = fieldName;
        this.read = read;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        final ReflectorExpression left = new DatabaseFieldExpression(fieldName);
        final ReflectorExpression right = new ParameterExpression(read);
        return visit(visitor, left, right);
    }

    protected abstract <T> T visit(
            final ReflectorPredicateVisitor<T> visitor,
            final ReflectorExpression left,
            final ReflectorExpression right
    );
}
