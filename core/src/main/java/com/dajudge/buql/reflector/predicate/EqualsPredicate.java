package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public class EqualsPredicate extends DatabaseFieldPredicate {
    public EqualsPredicate(final String fieldName, final Function<Object, Object> read) {
        super(fieldName, read);
    }

    @Override
    protected <T> T visit(
            final ReflectorPredicateVisitor<T> visitor,
            final ReflectorExpression left,
            final ReflectorExpression right
    ) {
        return visitor.eq(left, right);
    }
}
