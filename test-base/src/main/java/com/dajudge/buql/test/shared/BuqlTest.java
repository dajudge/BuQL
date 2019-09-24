package com.dajudge.buql.test.shared;

import com.dajudge.buql.BuQL;
import org.junit.Before;

import static com.dajudge.buql.test.shared.TestContainer.DIALECT;

public abstract class BuqlTest extends DatabaseTest {
    protected BuQL buql;

    @Before
    public void setupBuql() {
        buql = new BuQL(DIALECT, engine);
    }
}
