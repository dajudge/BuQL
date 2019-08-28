package com.dajudge.buql.reflector.translate;

import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.expression.QueryExpression;
import com.dajudge.buql.reflector.predicate.ReflectorExpressionVisitor;

import java.util.function.Function;

public class QueryExpressionVisitor implements ReflectorExpressionVisitor<QueryExpression> {
    private final FilterFieldsMapping filterFieldsMapping;

    public QueryExpressionVisitor(final FilterFieldsMapping filterFieldsMapping) {
        this.filterFieldsMapping = filterFieldsMapping;
    }

    @Override
    public QueryExpression parameter(final Function<Object, Object> reader) {
        return new FilterColExpression(filterFieldsMapping.create(reader));
    }

    @Override
    public QueryExpression databaseField(final String fieldName) {
        return new DataColExpression(fieldName);
    }
}
