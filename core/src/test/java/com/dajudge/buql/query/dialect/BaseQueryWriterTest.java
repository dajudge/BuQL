package com.dajudge.buql.query.dialect;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.model.QueryWithParameters;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.expression.QueryExpression;
import com.dajudge.buql.query.model.expression.QueryPredicate;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.query.model.select.SelectQuery;
import org.junit.Test;

import java.util.List;

import static com.dajudge.buql.query.model.expression.QueryCompareOperator.EQUALS;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BaseQueryWriterTest {
    private static final List<ProjectionColumn> PROJECTION_COLUMNS = asList(
            ProjectionColumn.fromFilters("FCOL1", "b")
    );
    private static final List<Object> FILTER_PARAMETERS = asList(42l);
    private static final List<String> FILTER_COLUMNS = asList("FCOL1");
    private static final QueryExpression EXPRESSION0 = new DataColExpression("DBCOL1");
    private static final QueryExpression EXPRESSION1 = new FilterColExpression("FCOL1");
    private static final QueryPredicate PREDICATE = QueryPredicate.compare(EXPRESSION0, EXPRESSION1, EQUALS);
    private static final String QUERY_TABLE = "queryTable";

    @Test
    public void writes_query() {
        assertQueryEquals(getExpectedSql());
    }

    public void assertQueryEquals(final String expectedSql) {
        final SelectQuery queryModel = new SelectQuery(
                PROJECTION_COLUMNS,
                FILTER_PARAMETERS,
                FILTER_COLUMNS,
                PREDICATE,
                QUERY_TABLE
        );
        final QueryWithParameters query = getDialect().select(queryModel).get(0);
        assertNotNull(query);
        assertEquals(expectedSql, query.getSql());
        assertEquals(FILTER_PARAMETERS, query.getQueryParameters());
    }

    protected abstract String getExpectedSql();

    protected abstract BaseDialect getDialect();
}