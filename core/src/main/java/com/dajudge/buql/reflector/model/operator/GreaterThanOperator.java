package com.dajudge.buql.reflector.model.operator;

import com.dajudge.buql.reflector.annotations.GreaterThan;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.GT;

public class GreaterThanOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return GT;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(GreaterThan.class) != null;
    }
}
