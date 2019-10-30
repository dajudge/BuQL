package com.dajudge.buql.analyzer.methodmatcher;

import com.dajudge.buql.analyzer.ReflectionUtil.TypeCaptor;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Optional.empty;

class TypeMatcher {
    private final TypeCaptor typeCaptor;
    private final Predicate<Type> typePredicate;

    TypeMatcher(final TypeCaptor typeCaptor, final Predicate<Type> typePredicate) {
        this.typeCaptor = typeCaptor;
        this.typePredicate = typePredicate;
    }

    Optional<Type> match(final Type type) {
        return typePredicate.test(type) ? typeCaptor.getCapturedType() : empty();
    }
}
