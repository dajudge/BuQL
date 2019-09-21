package com.dajudge.buql.analyzer;

import com.dajudge.buql.analyzer.ComplexResultTypeModel.ResultFieldModel;
import com.dajudge.buql.reflector.annotations.Transient;
import com.dajudge.buql.reflector.model.ResultField;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ComplexResultFieldsAnalyzer {
    private ComplexResultFieldsAnalyzer() {
    }

    public static <R> List<ResultFieldModel<R>> createComplexResultFieldsAnalyzer(final Class<R> resultType) {
        try {
            final BeanInfo resultBean = Introspector.getBeanInfo(resultType);
            return Stream.of(resultBean.getPropertyDescriptors())
                    .filter(prop -> prop.getWriteMethod() != null)
                    .filter(prop -> prop.getWriteMethod().getAnnotation(Transient.class) == null)
                    .map(ComplexResultFieldsAnalyzer::<R>toResultField)
                    .collect(toList());
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Failed to introspect result type " + resultType.getName(), e);
        }
    }

    private static <R> ResultFieldModel<R> toResultField(final PropertyDescriptor prop) {
        return new ResultFieldModel<R>() {
            @Override
            public ResultField<R> getResultField() {
                return new ResultField<>(prop.getName(), prop.getName());
            }

            @Override
            public void setFieldValue(final R object, final Object value) {
                try {
                    prop.getWriteMethod().invoke(object, value);
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to invoke setter " + prop.getWriteMethod(), e);
                }
            }
        };

    }
}
