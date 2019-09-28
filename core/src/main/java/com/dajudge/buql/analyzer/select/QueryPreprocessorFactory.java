package com.dajudge.buql.analyzer.select;

import java.util.Map;
import java.util.function.Function;

public interface QueryPreprocessorFactory {
    <T> Function<Object, Map<String, T>> createPreProcessor();
}
