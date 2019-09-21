package com.dajudge.buql.query.dialect;

import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.*;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;

public interface Dialect {
    QueryWithParameters select(SelectQuery selectQuery);

    String and(ProjectionSources sources, AndPredicate andPredicate);

    String or(ProjectionSources sources, OrPredicate predicate);

    String dataColumn(ProjectionSources sources, DataColExpression dataColExpression);

    String filterColumn(ProjectionSources sources, FilterColExpression filterColExpression);

    String constTrue();

    String compareOperator(
            ProjectionSources sources,
            BinaryPredicate predicate,
            SqlCompareOperator op
    );
}
