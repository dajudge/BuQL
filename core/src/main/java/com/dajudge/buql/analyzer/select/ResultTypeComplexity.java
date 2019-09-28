package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.ComplexResultTypeModel;
import com.dajudge.buql.analyzer.PrimitiveResultTypeModel;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;
import static com.dajudge.buql.analyzer.ReflectionUtil.isPrimitiveType;

public enum ResultTypeComplexity {
    PRIMITIVE("[A-Z].*", PrimitiveResultTypeModel::create, isPrimitiveType()),
    COMPLEX("", ComplexResultTypeModel::create, isComplexType());

    private final String pattern;
    private final ResultTypeToPredicateFactory factory;
    private final Predicate<Type> predicate;

    ResultTypeComplexity(
            final String pattern,
            final ResultTypeToPredicateFactory factory,
            final Predicate<Type> predicate
    ) {
        this.pattern = pattern;
        this.factory = factory;
        this.predicate = predicate;
    }

    String getResultTypePattern() {
        return pattern;
    }

    public ResultTypeToPredicateFactory getPredicateFactory() {
        return factory;
    }

    public Predicate<Type> getPredicate() {
        return predicate;
    }
}
