package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.predicate.ComplexQueryTypePredicate;
import com.dajudge.buql.analyzer.select.BulkQueryPreProcessor;
import com.dajudge.buql.reflector.ReflectDatabaseOperation;
import com.dajudge.buql.reflector.ReflectDatabaseOperation.Callback;
import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.select.MethodSelectModel;
import com.dajudge.buql.reflector.model.select.ResultTypeModel;
import com.dajudge.buql.reflector.predicate.ReflectorPredicate;
import com.dajudge.buql.test.shared.DatabaseTest;
import com.dajudge.buql.test.shared.model.FullResultObject;
import com.dajudge.buql.test.shared.model.SimpleQueryObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.dajudge.buql.reflector.model.select.SelectMethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.test.shared.TestContainer.DIALECT;

public class SimpleSelectTest extends DatabaseTest {

    @Test
    public void executesQuery() {
        final Class<SimpleQueryObject> queryType = SimpleQueryObject.class;
        final Class<FullResultObject> resultType = FullResultObject.class;
        final Function<Map<String, List<FullResultObject>>, ? extends Object> postProcessor = Function.identity();
        final ReflectorPredicate predicate = ComplexQueryTypePredicate.create(queryType);
        final ResultTypeModel<FullResultObject> resultTypeModel = ComplexResultTypeModel.create(resultType, null);
        final BulkQueryPreProcessor<SimpleQueryObject> preProcessor = new BulkQueryPreProcessor<>();
        final MethodSelectModel<SimpleQueryObject, FullResultObject> model = new MethodSelectModel<>(
                predicate,
                "mytable",
                resultTypeModel.getResultFields(),
                resultTypeModel::newResultInstance,
                preProcessor,
                postProcessor
        );
        final ReflectSelectQuery<SimpleQueryObject, FullResultObject> query = translateMethodModelToQuery(model);
        final Map<String, SimpleQueryObject> params = new HashMap<String, SimpleQueryObject>() {{
            put("ID0", new SimpleQueryObject(42));
            put("ID1", new SimpleQueryObject(43));
            put("ID2", new SimpleQueryObject(44));
        }};
        final Callback<FullResultObject> cb = new Callback<FullResultObject>() {
            @Override
            public void onResult(final String id, final FullResultObject value) {
                System.out.println(id + " -> " + value);
            }

            @Override
            public void done() {
                System.out.println("Done");
            }

            @Override
            public void onError(final Exception e) {
                throw new RuntimeException(e);
            }

            @Override
            public Map<String, List<FullResultObject>> getResult() {
                return null;
            }
        };
        query.execute(DIALECT, engine, params, cb);
    }
}
