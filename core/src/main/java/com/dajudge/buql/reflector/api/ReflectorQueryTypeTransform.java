package com.dajudge.buql.reflector.api;

import java.beans.PropertyDescriptor;

public interface ReflectorQueryTypeTransform {
    boolean appliesTo(PropertyDescriptor prop);

    Object apply(Object o);
}
