package com.dajudge.buql.query.model;

import com.dajudge.buql.query.dialect.Dialect;

public interface Query {
    QueryWithParameters toSelectQuery(Dialect dialect);

}
