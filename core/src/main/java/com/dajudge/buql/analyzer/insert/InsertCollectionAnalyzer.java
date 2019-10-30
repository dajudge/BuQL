package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.ReflectionUtil;

import static com.dajudge.buql.analyzer.ReflectionUtil.isComplexType;

public class InsertCollectionAnalyzer extends BaseInsertAnalyzer {
    public InsertCollectionAnalyzer() {
        super(ReflectionUtil::listOf, c -> c.captureIf(isComplexType()));
    }


    @Override
    public <Q> BulkInsertPreProcessor<Q> createPreProcessor() {
        return new BulkInsertPreProcessor<>();
    }
}
