package com.dajudge.buql.analyzer.methodmatcher;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;

public class MethodMatcherResult {
    private final Matcher methodNameMatcher;
    private final List<Type> parameterTypes;
    private final Type returnType;

    MethodMatcherResult(
            final Matcher methodNameMatcher,
            final List<Type> parameterTypes,
            final Type returnType
    ) {
        this.methodNameMatcher = methodNameMatcher;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    public Matcher methodNameMatcher() {
        return methodNameMatcher;
    }

    public Type getParameterType(final int index) {
        return parameterTypes.get(index);
    }

    public Type getReturnType() {
        return returnType;
    }
}
