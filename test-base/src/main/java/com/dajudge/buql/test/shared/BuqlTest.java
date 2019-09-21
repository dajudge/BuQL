package com.dajudge.buql.test.shared;

import com.dajudge.buql.BuQL;
import org.junit.Before;

public abstract class BuqlTest extends DatabaseTest {
    protected BuQL buql;

    @Before
    public void setupBuql() {
        assert dialect != null;
        assert engine != null;
        buql = new BuQL(dialect, engine);
    }
}
