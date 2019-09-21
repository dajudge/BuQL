package com.dajudge.buql.test.shared.model;

import com.dajudge.buql.reflector.annotations.Transient;

public class SimpleQueryObject {
    private final long longValue;

    public SimpleQueryObject(final long longValue) {
        this.longValue = longValue;
    }

    public long getLongValue() {
        return longValue;
    }

    @Transient
    public String getQueryIrrelevantValue() {
        return null;
    }
}
