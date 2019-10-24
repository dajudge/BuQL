package com.dajudge.buql.query.model;

public interface QueryModel<Q, T extends Query> {
    T createQuery(Q queryObject);
}
