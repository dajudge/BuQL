package com.dajudge.buql.query.dialect.h2;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.dialect.BaseQueryWriterTest;

public class H2SelectQueryWriterTest extends BaseQueryWriterTest {
    @Override
    protected String getExpectedSql() {
        return "SELECT C1 AS \"b\" FROM (VALUES (?)), queryTable D WHERE (D.DBCOL1 = C1)";
    }

    @Override
    protected BaseDialect getDialect() {
        return new H2Dialect();
    }
}