package com.dajudge.buql.reflector.model.operator;

import com.dajudge.buql.reflector.annotations.LessThan;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.LT;

public class LessThanOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return LT;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(LessThan.class) != null;
    }
}
