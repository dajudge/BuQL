package com.dajudge.buql.query.dialect;

import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.insert.InsertQuery;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;
import com.dajudge.buql.query.model.select.SelectQuery;

import java.util.List;

public interface Dialect {
    List<QueryWithParameters> select(SelectQuery selectQuery);

    List<QueryWithParameters> insert(InsertQuery insertQuery);

    String dataColumn(ProjectionSources sources, DataColExpression dataColExpression);

    String filterColumn(ProjectionSources sources, FilterColExpression filterColExpression);

    String constTrue();

    String compareOperator(
            final String left,
            final String right,
            final SqlCompareOperator op
    );

    String booleanOperation(List<String> operands, String operator);

    String isNull(String operand);
}
