package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.base.select.BaseSelectQuerySerializer;
import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;
import com.dajudge.buql.query.dialect.base.select.impl.ConstructorFilterTableSerializer;
import com.dajudge.buql.query.dialect.base.select.impl.DataTableWithAliasSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

public class H2SelectQuerySerializer extends BaseSelectQuerySerializer {
    private final SelectQuery queryModel;

    public H2SelectQuerySerializer(final SelectQuery queryModel, final H2Dialect dialect) {
        super(dialect, queryModel);
        this.queryModel = queryModel;
    }

    @Override
    protected DataTableSerializer getDataTable() {
        return new DataTableWithAliasSerializer(queryModel, "D");
    }

    @Override
    protected FilterTableSerializer getFilterTable() {
        return new ConstructorFilterTableSerializer(
                new H2ValuesConstructorSerializer(queryModel)
        );
    }
}
