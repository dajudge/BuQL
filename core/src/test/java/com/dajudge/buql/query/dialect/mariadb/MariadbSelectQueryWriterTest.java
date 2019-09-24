package com.dajudge.buql.query.dialect.mariadb;

import com.dajudge.buql.query.dialect.base.BaseDialect;
import com.dajudge.buql.query.dialect.BaseQueryWriterTest;

public class MariadbSelectQueryWriterTest extends BaseQueryWriterTest {
    @Override
    protected String getExpectedSql() {
        return "WITH F(FCOL1) AS (VALUES (?)) SELECT F.FCOL1 AS \"b\" FROM F, queryTable D WHERE (D.DBCOL1 = F.FCOL1)";
    }

    @Override
    protected BaseDialect getDialect() {
        return new MariadbDialect();
    }
}