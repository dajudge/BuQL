package com.dajudge.buql.test.jdbc;

public class FullResultObject {
    private long pk;
    private String stringValue;
    private long longValue;

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }

    public long getPk() {
        return pk;
    }

    public void setPk(final long pk) {
        this.pk = pk;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(final long longValue) {
        this.longValue = longValue;
    }

    @Override
    public String toString() {
        return "FullResultObject{" +
                "pk=" + pk +
                ", stringValue='" + stringValue + '\'' +
                ", longValue=" + longValue +
                '}';
    }
}
