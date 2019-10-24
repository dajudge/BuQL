package com.dajudge.buql.query.model;

import com.dajudge.buql.query.dialect.Dialect;

import java.util.List;

public interface Query {
    List<QueryWithParameters> toQueryBatch(Dialect dialect);

}
