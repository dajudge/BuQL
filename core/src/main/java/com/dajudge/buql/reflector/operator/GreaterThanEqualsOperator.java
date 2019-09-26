package com.dajudge.buql.reflector.operator;

import com.dajudge.buql.reflector.annotations.GreaterThanEquals;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.GTE;

public class GreaterThanEqualsOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return GTE;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(GreaterThanEquals.class) != null;
    }
}
