package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.query.engine.QueryExecutor;
import com.dajudge.buql.query.model.Query;
import com.dajudge.buql.query.model.expression.DataColExpression;
import com.dajudge.buql.query.model.expression.FilterColExpression;
import com.dajudge.buql.query.model.expression.QueryPredicate;
import com.dajudge.buql.query.model.select.ProjectionColumn;
import com.dajudge.buql.query.model.select.SelectQuery;
import com.dajudge.buql.test.jdbc.matcher.QueryResult;
import com.dajudge.buql.test.jdbc.matcher.QueryResultAssertions;
import com.dajudge.buql.test.shared.DatabaseTest;
import com.dajudge.buql.test.shared.TestContainer;
import org.junit.Test;

import java.util.List;

import static com.dajudge.buql.query.model.expression.QueryCompareOperator.EQUALS;
import static com.dajudge.buql.query.model.expression.QueryPredicate.*;
import static com.dajudge.buql.test.jdbc.matcher.QueryResult.into;
import static com.dajudge.buql.test.shared.TestContainer.DIALECT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class JdbcDatabaseEngineTest extends DatabaseTest {

    @Test
    public void forwards_exceptions() {
        assertWith("bogus", emptyList())
                .hasException();
    }

    @Test
    public void executes_bulk_with_all_rows() {
        final List<ProjectionColumn> projectionColumns = asList(
                ProjectionColumn.fromFilters("ID", "ID"),
                ProjectionColumn.fromData("PK", "P0")
        );
        final List<Object> filterParameters = asList(
                "ID0"
        );
        final List<String> filterColumns = asList("ID");
        final QueryPredicate predicate = constTrue();
        final Query query = new SelectQuery(projectionColumns, filterParameters, filterColumns, predicate, "mytable");
        assertWith(query)
                .hasCount(2)
                .hasNoException();
    }

    @Test
    public void executes_bulk_query_with_multiple_filter_rows() {
        final List<ProjectionColumn> projectionColumns = asList(
                ProjectionColumn.fromFilters("ID_LABEL", "ID_LABEL"),
                ProjectionColumn.fromData("PK", "P0")
        );
        final List<Object> filterParameters = asList(
                "ID0", "v1",
                "ID1", "v2"
        );
        final List<String> filterColumns = asList("ID_LABEL", "F0");
        final QueryPredicate predicate = compare(new DataColExpression("stringValue"), new FilterColExpression("F0"), EQUALS);
        final Query query = new SelectQuery(projectionColumns, filterParameters, filterColumns, predicate, "mytable");
        assertWith(query)
                .hasNoException()
                .hasRow()
                .hasColumun("ID_LABEL", "ID0")
                .hasColumun("P0", 1L)
                .endRow()
                .hasCount(1);
    }

    @Test
    public void executes_bulk_query_with_one_filter_row() {
        final List<ProjectionColumn> projectionColumns = asList(
                ProjectionColumn.fromFilters("ID", "id"),
                ProjectionColumn.fromData("PK", "p0")
        );
        final List<Object> filterParameters = asList(
                "v1", "ID0"
        );
        final QueryPredicate predicate = and(
                compare(new DataColExpression("stringValue"), new FilterColExpression("F0"), EQUALS)
        );
        final List<String> filterColumns = asList("F0", "ID");
        final Query query = new SelectQuery(projectionColumns, filterParameters, filterColumns, predicate, "mytable");
        assertWith(query)
                .hasNoException()
                .hasRow()
                .hasColumun("id", "ID0")
                .hasColumun("p0", 1L)
                .endRow()
                .hasCount(1);
    }

    private QueryResultAssertions assertWith(final Query query) {
        final QueryResult queryResult = new QueryResult();
        new QueryExecutor(engine, DIALECT).execute(query, into(queryResult));
        return QueryResultAssertions.assertWith(queryResult);
    }

    private QueryResultAssertions assertWith(final String query, final List<Object> params) {
        final QueryResult queryResult = new QueryResult();
        engine.executeQuery(query, params, into(queryResult));
        return QueryResultAssertions.assertWith(queryResult);
    }

}
