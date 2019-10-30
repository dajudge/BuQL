package com.dajudge.buql.analyzer.methodmatcher;

import com.dajudge.buql.analyzer.ReflectionUtil.TypeCaptor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class MethodMatcherBuilder {
    private final Pattern methodNamePattern;
    private final List<TypeMatcher> parameterTypeMatchers = new ArrayList<>();
    private TypeMatcher returnTypeMatcher;

    private MethodMatcherBuilder(final Pattern methodNamePattern) {
        this.methodNamePattern = methodNamePattern;
    }

    public static MethodMatcherBuilder newMatcher(final Pattern methodNamePattern) {
        return new MethodMatcherBuilder(methodNamePattern);
    }

    public MethodMatcher build() {
        return new MethodMatcher(methodNamePattern, parameterTypeMatchers, returnTypeMatcher);
    }

    public MethodMatcherBuilder requiresParam(
            final Function<Predicate<Type>, Predicate<Type>> multiplicityTransform,
            final Function<TypeCaptor, Predicate<Type>> typePredicate
    ) {
        parameterTypeMatchers.add(createTypeMatcher(multiplicityTransform, typePredicate));
        return this;
    }

    public MethodMatcherBuilder requiresReturnType(
            final Function<Predicate<Type>, Predicate<Type>> multiplicityTransform,
            final Function<TypeCaptor, Predicate<Type>> typePredicate
    ) {
        returnTypeMatcher = createTypeMatcher(multiplicityTransform, typePredicate);
        return this;
    }

    private static TypeMatcher createTypeMatcher(
            final Function<Predicate<Type>, Predicate<Type>> multiplicityTransform,
            final Function<TypeCaptor, Predicate<Type>> typePredicate
    ) {
        final TypeCaptor captor = new TypeCaptor();
        final Predicate<Type> parameterPredicate = multiplicityTransform.apply(typePredicate.apply(captor));
        return new TypeMatcher(captor, parameterPredicate);
    }
}
