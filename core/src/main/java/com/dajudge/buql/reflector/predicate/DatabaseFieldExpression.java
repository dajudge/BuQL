package com.dajudge.buql.reflector.predicate;

public class DatabaseFieldExpression implements ReflectorExpression {

    private final String colName;

    public DatabaseFieldExpression(final String colName) {
        this.colName = colName;
    }

    @Override
    public <T> T visit(final ReflectorExpressionVisitor<T> visitor) {
        return visitor.databaseField(colName);
    }
}
