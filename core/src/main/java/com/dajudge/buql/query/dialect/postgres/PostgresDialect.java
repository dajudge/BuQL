package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.BaseDialect;
import com.dajudge.buql.query.dialect.ColRenamer;
import com.dajudge.buql.query.dialect.SelectQueryWriter;
import com.dajudge.buql.query.dialect.ValuesWriter;
import com.dajudge.buql.query.model.select.ProjectionColumn;

public class PostgresDialect extends BaseDialect {
    public PostgresDialect() {
    }

    @Override
    protected SelectQueryWriter getSelectQueryWriter() {
        return new SelectQueryWriter(
                this,
                new ValuesWriter("F"),
                () -> ColRenamer.IDENTITY,
                () -> ColRenamer.IDENTITY
        ) {
            @Override
            protected ProjectionColumn.ProjectionSources sources(
                    final ColRenamer filterColRenamer,
                    final ColRenamer dataColRenamer
            ) {
                return new ProjectionColumn.ProjectionSources() {
                    @Override
                    public String getFiltersColumn(final String filterColumn) {
                        return "F." + filterColRenamer.resolve(filterColumn);
                    }

                    @Override
                    public String getDataColumn(final String dataColumn) {
                        return "D." + dataColRenamer.resolve(dataColumn);
                    }
                };
            }
        };
    }
}
