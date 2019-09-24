package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.dialect.base.select.BaseSelectQuerySerializer;
import com.dajudge.buql.query.dialect.base.select.DataTableSerializer;
import com.dajudge.buql.query.dialect.base.select.FilterTableSerializer;
import com.dajudge.buql.query.dialect.base.select.impl.DataTableWithAliasSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

public class MariadbSelectQuerySerializer extends BaseSelectQuerySerializer {
    private final SelectQuery queryModel;
    private final String filterTableAlias;

    public MariadbSelectQuerySerializer(
            final MariadbDialect dialect,
            final SelectQuery queryModel,
            final String filterTableAlias
    ) {
        super(dialect, queryModel);
        this.queryModel = queryModel;
        this.filterTableAlias = filterTableAlias;
    }

    @Override
    protected DataTableSerializer getDataTable() {
        return new DataTableWithAliasSerializer(queryModel, "D");
    }

    @Override
    protected FilterTableSerializer getFilterTable() {
        return new FilterTableSerializer() {
            @Override
            public String getSql() {
                return filterTableAlias;
            }

            @Override
            public String referenceField(final String columnName) {
                return filterTableAlias + "." + columnName;
            }
        };
    }
}
