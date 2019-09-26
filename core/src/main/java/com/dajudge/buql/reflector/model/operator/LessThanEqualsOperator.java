package com.dajudge.buql.reflector.model.operator;

import com.dajudge.buql.reflector.annotations.LessThanEquals;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.LTE;

public class LessThanEqualsOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return LTE;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(LessThanEquals.class) != null;
    }
}
