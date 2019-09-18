package com.dajudge.buql.reflector.predicate;

public class DatabaseFieldExpression implements ReflectorExpression {
    private final String fieldName;

    public DatabaseFieldExpression(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.databaseField(fieldName);
    }
}
