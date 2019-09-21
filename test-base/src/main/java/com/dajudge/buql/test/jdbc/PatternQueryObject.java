package com.dajudge.buql.test.jdbc;

import com.dajudge.buql.reflector.annotations.Like;

public class PatternQueryObject {
    private final String stringValue;

    public PatternQueryObject(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Like
    public String getStringValue() {
        return stringValue;
    }
}
