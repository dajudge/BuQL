package com.dajudge.buql.query.dialect.postgres;

import com.dajudge.buql.query.dialect.BaseQueryWriterTest;
import com.dajudge.buql.query.dialect.base.BaseDialect;

public class PostgresSelectQueryWriterTest extends BaseQueryWriterTest {
    @Override
    protected String getExpectedSql() {
        return "SELECT F.FCOL1 AS \"b\" FROM (VALUES (?)) AS F(FCOL1), queryTable D WHERE (D.DBCOL1 = F.FCOL1)";
    }

    @Override
    protected BaseDialect getDialect() {
        return new PostgresDialect();
    }
}