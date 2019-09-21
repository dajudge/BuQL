package com.dajudge.buql.postgis;

import com.dajudge.buql.postgis.annotation.Covers;
import com.dajudge.buql.query.dialect.SqlCompareOperator;
import com.dajudge.buql.query.model.expression.QueryCompareOperator;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

public class CoversReflectorOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return () -> (QueryCompareOperator) () -> (SqlCompareOperator) (operand1, operand2) ->
                "ST_Covers(" + operand1 + ", " + operand2 + ")";
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(Covers.class) != null;
    }
}
