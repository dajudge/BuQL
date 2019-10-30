package com.dajudge.buql.analyzer.insert;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;
import static java.util.function.Function.identity;

public class InsertSingleAnalyzer extends BaseInsertAnalyzer {
    public InsertSingleAnalyzer() {
        super(identity(), c -> c.captureIf(isComplexType()));
    }

    @Override
    public <Q> SingleInsertPreProcessor<Q> createPreProcessor() {
        return new SingleInsertPreProcessor<>();
    }
}
