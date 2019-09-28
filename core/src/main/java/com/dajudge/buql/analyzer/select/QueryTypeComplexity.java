package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.predicates.ComplexQueryTypePredicate;
import com.dajudge.buql.analyzer.predicates.PrimitiveQueryTypePredicate;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;
import static com.dajudge.buql.analyzer.ReflectionUtil.isPrimitiveType;

public enum QueryTypeComplexity {
    PRIMITIVE((actualQueryType, predicateName) -> {
        return PrimitiveQueryTypePredicate.create(null, predicateName);
    }, "[A-Z].*", isPrimitiveType()),
    COMPLEX((actualQueryType, predicateName) -> {
        return ComplexQueryTypePredicate.create(actualQueryType, null);
    }, "[A-Z].*", isComplexType());

    private final ReflectorPredicateFactory reflectorPredicateFactory;
    private final String pattern;
    private final Predicate<Type> predicate;

    QueryTypeComplexity(
            final ReflectorPredicateFactory reflectorPredicateFactory,
            final String pattern,
            final Predicate<Type> predicate
    ) {
        this.reflectorPredicateFactory = reflectorPredicateFactory;
        this.pattern = pattern;
        this.predicate = predicate;
    }

    public ReflectorPredicateFactory getPredicateFactory() {
        return reflectorPredicateFactory;
    }

    public String getQueryTypePattern() {
        return pattern;
    }

    public Predicate<Type> getPredicate() {
        return predicate;
    }
}
