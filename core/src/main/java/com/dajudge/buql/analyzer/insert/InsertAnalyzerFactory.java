package com.dajudge.buql.analyzer.insert;

import com.dajudge.buql.analyzer.api.Analyzer;
import com.dajudge.buql.analyzer.api.AnalyzerFactory;

import java.util.stream.Stream;

public class InsertAnalyzerFactory implements AnalyzerFactory {
    @Override
    public Stream<Analyzer> create() {
        return Stream.of(
                new InsertCollectionAnalyzer(),
                new InsertSingleAnalyzer()
        );
    }
}
