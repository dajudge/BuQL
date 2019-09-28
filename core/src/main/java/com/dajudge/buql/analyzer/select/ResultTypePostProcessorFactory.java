package com.dajudge.buql.analyzer.select;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

interface ResultTypePostProcessorFactory {
    <T> Function<Map<String, List<T>>, Map<String, ?>> getPostProcessor();
}
