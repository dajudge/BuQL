package com.dajudge.buql.reflector.operator;

import com.dajudge.buql.reflector.annotations.NotEquals;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.NOT_EQUALS;

public class NotEqualsOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return NOT_EQUALS;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(NotEquals.class) != null;
    }
}
