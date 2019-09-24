package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.ColRenamer;
import com.dajudge.buql.query.dialect.base.select.impl.ValuesConstructorSerializer;
import com.dajudge.buql.query.model.select.SelectQuery;

public class H2ValuesConstructorSerializer extends ValuesConstructorSerializer {
    private final ColRenamer colRenamer;

    public H2ValuesConstructorSerializer(
            final SelectQuery queryModel
    ) {
        super(queryModel);
        this.colRenamer = new IncrementingColRenamer("C", 1, queryModel.getFilterColumns());
    }

    @Override
    public String referenceField(final String filterColumn) {
        return colRenamer.resolve(filterColumn);
    }
}
