package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;

public class InsertSingleAnalyzer extends BaseInsertAnalyzer {
    @Override
    public Predicate<Type> getQueryTypeExtractor(final ReflectionUtil.TypeCaptor captor) {
        return captor.captureIf(isComplexType());
    }

    @Override
    public <Q> SingleInsertPreProcessor<Q> createPreProcessor() {
        return new SingleInsertPreProcessor<>();
    }
}
