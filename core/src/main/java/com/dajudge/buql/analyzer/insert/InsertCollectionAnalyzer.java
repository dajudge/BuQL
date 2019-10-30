package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;
import static com.dajudge.buql.analyzer.ReflectionUtil.listOf;

public class InsertCollectionAnalyzer extends BaseInsertAnalyzer {
    @Override
    public Predicate<Type> getQueryTypeExtractor(final ReflectionUtil.TypeCaptor captor) {
        return listOf(captor.captureIf(isComplexType()));
    }

    @Override
    public <Q> BulkInsertPreProcessor<Q> createPreProcessor() {
        return new BulkInsertPreProcessor<>();
    }
}
