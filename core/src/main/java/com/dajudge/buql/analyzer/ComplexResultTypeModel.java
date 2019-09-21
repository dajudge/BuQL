package com.dajudge.buql.analyzer;

import com.dajudge.buql.reflector.model.MethodSelectModelFactory;
import com.dajudge.buql.reflector.model.ResultField;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dajudge.buql.analyzer.ComplexResultFieldsAnalyzer.createComplexResultFieldsAnalyzer;
import static java.util.stream.Collectors.toList;

public class ComplexResultTypeModel<R> implements MethodSelectModelFactory.ResultTypeModel<R> {
    public interface ResultFieldModel<R> {
           ResultField<R> getResultField();
           void setFieldValue(R object, Object value);
    }
    private final List<ResultFieldModel<R>> fields;
    private final Class<R> clazz;

    public ComplexResultTypeModel(final Class<R> clazz) {
        this.clazz = clazz;
        this.fields = createComplexResultFieldsAnalyzer(clazz);
    }

    @Override
    public List<ResultField<R>> getResultFields() {
        return fields.stream().map(ResultFieldModel::getResultField).collect(toList());
    }


    @Override
    public R newResultInstance(final Function<String, Object> columnValues) {
        final R ret = newInstanceOf(clazz);
        fields.forEach(field -> field.setFieldValue(ret, columnValues.apply(field.getResultField().getTableColumn())));
        return ret;
    }

    private R newInstanceOf(final Class<R> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot instantiate result type instance", e);
        }
    }
}
