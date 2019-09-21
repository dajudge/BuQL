package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public class ParameterExpression implements ReflectorExpression {
    private final Function<Object, Object> read;
    private final Function<Object, Object> transform;

    public ParameterExpression(final Function<Object, Object> read, final Function<Object, Object> transform) {
        this.read = read;
        this.transform = transform;
    }


    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.parameter(read.andThen(transform));
    }
}
