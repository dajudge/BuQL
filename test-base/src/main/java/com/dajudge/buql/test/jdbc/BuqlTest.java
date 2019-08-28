package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.BuQL;
import org.junit.Before;

public abstract class BuqlTest extends DatabaseTest {
    BuQL buql;

    @Before
    public void setupBuql() {
        System.out.println("buql");
        assert dialect != null;
        assert engine != null;
        buql = new BuQL(dialect, engine);
    }
}
