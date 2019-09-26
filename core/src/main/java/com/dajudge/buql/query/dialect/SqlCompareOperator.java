package com.dajudge.buql.query.dialect;

public interface SqlCompareOperator {

    SqlCompareOperator GTE = new InfixOperator(">=");
    SqlCompareOperator EQUALS = new InfixOperator("=");
    SqlCompareOperator LIKE = new InfixOperator("LIKE");

    String toSql(String operand1, String operand2);

    class InfixOperator implements SqlCompareOperator {
        private final String operatorLiteral;

        private InfixOperator(final String operatorLiteral) {
            this.operatorLiteral = operatorLiteral;
        }

        @Override
        public String toSql(final String left, final String right) {
            return "(" + left + " " + operatorLiteral + " " + right + ")";
        }
    }
}
