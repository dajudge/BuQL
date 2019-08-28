package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.reflector.ReflectSelectQuery;
import com.dajudge.buql.reflector.model.MethodSelectModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dajudge.buql.reflector.model.MethodModelTranslator.translateMethodModelToQuery;
import static com.dajudge.buql.reflector.model.MethodSelectModelFactory.createSelectModel;

public class SimpleSelectTest extends DatabaseTest {

    @Test
    public void executesQuery() {
        final MethodSelectModel<SimpleQueryObject, FullResultObject> model = createSelectModel(
                "mytable",
                SimpleQueryObject.class,
                FullResultObject.class
        );
        final ReflectSelectQuery<SimpleQueryObject, FullResultObject> query = translateMethodModelToQuery(model);
        final Map<String, SimpleQueryObject> params = new HashMap<String, SimpleQueryObject>() {{
            put("ID0", new SimpleQueryObject(42));
            put("ID1", new SimpleQueryObject(43));
            put("ID2", new SimpleQueryObject(44));
        }};
        query.execute(dialect, engine, params, new ReflectSelectQuery.Callback<FullResultObject>() {
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
        });
    }
}
