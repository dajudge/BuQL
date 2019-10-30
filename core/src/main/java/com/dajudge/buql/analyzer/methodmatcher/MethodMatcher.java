package com.dajudge.buql.analyzer.methodmatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodMatcher {
    private final Pattern methodNamePattern;
    private final List<TypeMatcher> parameterTypeMatchers;
    private final TypeMatcher returnTypeMatcher;

    MethodMatcher(
            final Pattern methodNamePattern,
            final List<TypeMatcher> parameterTypeMatchers,
            final TypeMatcher returnTypeMatcher
    ) {

        this.methodNamePattern = methodNamePattern;
        this.parameterTypeMatchers = parameterTypeMatchers;
        this.returnTypeMatcher = returnTypeMatcher;
    }

    public Optional<MethodMatcherResult> match(final Method method) {
        final Matcher methodNameMatcher = methodNamePattern.matcher(method.getName());
        if (!methodNameMatcher.matches()) {
            return Optional.empty();
        }
        if (method.getParameterCount() != parameterTypeMatchers.size()) {
            return Optional.empty();
        }
        final List<Type> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameterTypeMatchers.size(); i++) {
            final TypeMatcher typeMatcher = parameterTypeMatchers.get(i);
            final Optional<Type> parameterType = typeMatcher.match(method.getGenericParameterTypes()[i]);
            if (parameterType.isPresent()) {
                parameterTypes.add(parameterType.get());
            } else {
                return Optional.empty();
            }
        }
        if (returnTypeMatcher == null) {
            return Optional.of(new MethodMatcherResult(
                    methodNameMatcher,
                    parameterTypes,
                    null
            ));
        } else {
            final Optional<Type> returnType = returnTypeMatcher.match(method.getGenericReturnType());
            if (!returnType.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(new MethodMatcherResult(
                    methodNameMatcher,
                    parameterTypes,
                    returnType.get()
            ));
        }
    }
}
