package com.dajudge.buql.query.dialect;

public interface ColRenamer {
    ColRenamer IDENTITY = new ColRenamer() {
        @Override
        public String rename(final String in) {
            return in;
        }

        @Override
        public String resolve(final String in) {
            return in;
        }
    };

    String rename(String in);

    String resolve(String in);
}
