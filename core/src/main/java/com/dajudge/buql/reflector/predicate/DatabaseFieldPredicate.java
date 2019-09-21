package com.dajudge.buql.reflector.predicate;

import java.util.function.Function;

public class DatabaseFieldPredicate implements ReflectorPredicate {
    private final String colName;
    private final Function<Object, Object> read;
    private final ReflectorCompareOperator operator;
    private final Function<Object, Object> transform;

    public DatabaseFieldPredicate(
            final String colName,
            final Function<Object, Object> read,
            final ReflectorCompareOperator operator,
            final Function<Object, Object> transform
    ) {
        this.colName = colName;
        this.read = read;
        this.operator = operator;
        this.transform = transform;
    }

    @Override
    public <T> T visit(final ReflectorPredicateVisitor<T> visitor) {
        final ReflectorExpression left = new DatabaseFieldExpression(colName);
        final ReflectorExpression right = new ParameterExpression(read, transform);
        return visitor.compare(left, right, operator);
    }
}
