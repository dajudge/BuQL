package com.dajudge.buql.reflector.predicate;

public class DatabaseFieldExpecttion implements ReflectorExpression {
    private final String fieldName;

    public DatabaseFieldExpecttion(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.databaseField(fieldName);
    }
}
