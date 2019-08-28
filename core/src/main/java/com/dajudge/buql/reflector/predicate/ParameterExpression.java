package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public class ParameterExpression implements ReflectorExpression {
    private final Function<Object, Object> read;

    public ParameterExpression(final Function<Object, Object> read) {
        this.read = read;
    }


    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.parameter(read);
    }
}
