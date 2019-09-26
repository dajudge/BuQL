package com.dajudge.buql.reflector.predicate;

import com.dajudge.buql.reflector.annotations.Column;

import java.beans.PropertyDescriptor;
import java.util.Optional;

public class DatabaseFieldExpression implements ReflectorExpression {

    private final String name;

    public DatabaseFieldExpression(final PropertyDescriptor prop) {
        this(Optional.ofNullable(prop.getReadMethod().getAnnotation(Column.class))
                .map(Column::value)
                .orElse(prop.getName()));
    }

    public DatabaseFieldExpression(final String name) {
        this.name = name;
    }

    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.databaseField(name);
    }
}
