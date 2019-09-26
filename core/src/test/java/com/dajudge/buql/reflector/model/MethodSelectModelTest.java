package com.dajudge.buql.reflector.model;

import com.dajudge.buql.analyzer.BulkPreProcessor;
import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.predicates.ComplexQueryTypePredicate;
import com.dajudge.buql.query.dialect.postgres.PostgresDialect;
import com.dajudge.buql.query.engine.DatabaseEngine;
import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.annotations.BooleanOperator;
import com.dajudge.buql.reflector.model.MethodSelectModelFactory.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.dajudge.buql.reflector.annotations.BooleanOperationType.OR;
import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;

public class MethodSelectModelTest {

    @BooleanOperator(OR)
    public static class OrFilter {
        public OrFilter(final Condition1 condition1, final Condition2 condition2) {
            this.condition1 = condition1;
            this.condition2 = condition2;
        }

        private final Condition1 condition1;
        private final Condition2 condition2;

        public static class Condition1 {
            private final String stringValue;

            public Condition1(final String stringValue) {
                this.stringValue = stringValue;
            }

            public String getStringValue() {
                return stringValue;
            }
        }

        public static class Condition2 {
            private final int intValue;

            public Condition2(final int intValue) {
                this.intValue = intValue;
            }

            public int getIntValue() {
                return intValue;
            }
        }

        public Condition1 getCondition1() {
            return condition1;
        }

        public Condition2 getCondition2() {
            return condition2;
        }
    }

    public static class TestResultType {
        private String resultString;

        public String getResultString() {
            return resultString;
        }

        public void setResultString(final String resultString) {
            this.resultString = resultString;
        }

        @Override
        public String toString() {
            return "TestResultType{" +
                    "resultString='" + resultString + '\'' +
                    '}';
        }
    }

    @Test
    public void play() {
        final String tableName = "myTable";
        final ReflectorPredicate predicate = new ComplexQueryTypePredicate(OrFilter.class);
        final ResultTypeModel<TestResultType> resultTypeModel = new ComplexResultTypeModel<>(TestResultType.class);
        final Function<Object, Map<String, OrFilter>> preProcessor = new BulkPreProcessor<>();
        final MethodSelectModel model = createSelectModel(tableName, predicate, resultTypeModel, preProcessor, identity());
        final HashMap<String, OrFilter> params = new HashMap<String, OrFilter>() {{
            put("ID0", new OrFilter(new OrFilter.Condition1("testValue"), new OrFilter.Condition2(42)));
        }};
        final ReflectSelectQuery<OrFilter, TestResultType> query = translateMethodModelToQuery(model);
        final DatabaseEngine engine = new DatabaseEngine() {
            @Override
            public void executeQuery(final String query, final List<Object> params, final DatabaseResultCallback cb) {
                System.out.println(query);
                System.out.println(params);
                final List<Map<String, Object>> results = asList(new HashMap<String, Object>() {{
                    put("P_ID", "ID0");
                    put("resultString", "lolcats123");
                }});
                results.forEach(row -> {
                    cb.onRow(colName -> {
                        System.out.println(colName);
                        return row.get(colName);
                    });
                });
                cb.done();
            }

            @Override
            public void executeStatement(final String sql, final List<?> params, final DatabaseResultCallback cb) {
                throw new UnsupportedOperationException("Not implemented for this test");
            }
        };
        query.execute(new PostgresDialect(), engine, params, new ReflectSelectQuery.Callback<TestResultType>() {
            @Override
            public void onResult(final String id, final TestResultType value) {
                System.out.println(id + " -> " + value);
            }

            @Override
            public void done() {
                System.out.println("Done");
            }

            @Override
            public void onError(final Exception e) {
                System.out.println(e);
            }
        });
    }

}