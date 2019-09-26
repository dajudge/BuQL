package com.dajudge.buql.query.dialect.base.select.impl;

import com.dajudge.buql.query.QueryVisitor;
import com.dajudge.buql.query.dialect.Dialect;
import com.dajudge.buql.query.model.expression.BooleanOperation;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.expression.QueryComparePredicate;
import com.dajudge.buql.query.model.select.ProjectionColumn;

import static java.util.stream.Collectors.toList;

public class SqlSerializerVisitor implements QueryVisitor<String> {
    private final ProjectionColumn.ProjectionSources sources;
    private final Dialect dialect;

    public SqlSerializerVisitor(final ProjectionColumn.ProjectionSources sources, final Dialect dialect) {
        this.sources = sources;
        this.dialect = dialect;
    }

    @Override
    public String constantTrue() {
        return dialect.constTrue();
    }

    @Override
    public String dataCol(final DataColExpression dataColExpression) {
        return dialect.dataColumn(sources, dataColExpression);
    }

    @Override
    public String booleanOperation(final BooleanOperation booleanOperation) {
        return dialect.booleanOperation(
                booleanOperation.getOperands().stream().map(it -> it.visit(this)).collect(toList()),
                booleanOperation.getOperator().toString()
        );
    }

    @Override
    public String filterCol(final FilterColExpression filterColExpression) {
        return dialect.filterColumn(sources, filterColExpression);
    }

    @Override
    public String compare(final QueryComparePredicate queryComparePredicate) {
        return dialect.compareOperator(
                queryComparePredicate.getLeft().visit(this),
                queryComparePredicate.getRight().visit(this),
                queryComparePredicate.getOperator().getOperator()
        );
    }
}
