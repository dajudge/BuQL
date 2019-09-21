package com.dajudge.buql.reflector.model.operator;

import com.dajudge.buql.reflector.annotations.Like;
import com.dajudge.buql.reflector.api.ReflectorOperator;
import com.dajudge.buql.reflector.predicate.ReflectorCompareOperator;

import java.beans.PropertyDescriptor;

import static com.dajudge.buql.reflector.predicate.ReflectorCompareOperator.LIKE;

public class LikeOperator implements ReflectorOperator {
    @Override
    public ReflectorCompareOperator getOperator() {
        return LIKE;
    }

    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getAnnotation(Like.class) != null;
    }
}
