package com.dajudge.buql.postgis.transform;

import com.dajudge.buql.postgis.types.Point;
import com.dajudge.buql.reflector.api.ReflectorQueryTypeTransform;

import java.beans.PropertyDescriptor;

public class PointTransform implements ReflectorQueryTypeTransform {
    @Override
    public boolean appliesTo(final PropertyDescriptor prop) {
        return prop.getReadMethod().getGenericReturnType() == Point.class;
    }

    @Override
    public Object apply(final Object o) {
        final Point p = (Point) o;
        return "POINT(" + p.getX() + " " + p.getY() + ")";
    }
}
