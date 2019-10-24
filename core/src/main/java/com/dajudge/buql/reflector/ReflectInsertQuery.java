package com.dajudge.buql.reflector;

import com.dajudge.buql.query.engine.DatabaseResultCallback;
import com.dajudge.buql.query.model.insert.InsertQueryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.dajudge.buql.reflector.ReflectDatabaseOperation.Callback.nullCallback;
import static java.util.function.Function.identity;

public class ReflectInsertQuery<Q> extends ReflectStatement<Q, List<Q>, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectInsertQuery.class);

    public ReflectInsertQuery(
            final InsertQueryModel<Q> queryModel,
            final Function<Object, List<Q>> preProcessor
    ) {
        super(queryModel, preProcessor, identity());
    }

    @Override
    protected Consumer<DatabaseResultCallback.ResultRow> rowMapperFor(final Callback<Void> callback) {
        return row -> LOG.debug("Inserted {} rows", row.getObject("lolcats"));
    }

    @Override
    public Callback<Void> createCallback(final List<Q> params) {
        return nullCallback();
    }
}
