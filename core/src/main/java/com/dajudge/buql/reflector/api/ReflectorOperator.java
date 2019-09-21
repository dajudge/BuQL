package com.dajudge.buql.reflector.api;

import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

public interface ReflectorOperator {
    ReflectorCompareOperator getOperator();

    boolean appliesTo(PropertyDescriptor prop);
}
