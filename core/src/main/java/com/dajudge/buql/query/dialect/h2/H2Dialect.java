package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.*;
import com.dajudge.buql.query.model.select.ProjectionColumn.ProjectionSources;

import static com.dajudge.buql.query.dialect.ColRenamer.IDENTITY;

public class H2Dialect extends BaseDialect {

    public H2Dialect() {
    }

    @Override
    protected SelectQueryWriter getSelectQueryWriter() {
        return new SelectQueryWriter(
                this,
                new ValuesWriter(null),
                () -> new IncrementingColRenamer("C", 1),
                () -> IDENTITY
        ) {
            @Override
            protected ProjectionSources sources(final ColRenamer filterColRenamer, final ColRenamer dataColRenamer) {
                return new ProjectionSources() {
                    @Override
                    public String getFiltersColumn(final String filterColumn) {
                        return filterColRenamer.resolve(filterColumn);
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
