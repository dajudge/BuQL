package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.analyzer.api.Analyzer;
import com.dajudge.buql.analyzer.api.AnalyzerFactory;

import java.util.stream.Stream;

import static java.util.function.Function.identity;

public class SelectAnalyzerFactory implements AnalyzerFactory {
    @Override
    public Stream<Analyzer> create() {
        return Stream.of(QueryMultiplicity.values()).map(queryMultiplicity ->
                Stream.of(QueryTypeComplexity.values()).map(queryTypeComplexity ->
                        Stream.of(ResultMultiplicity.values()).map(resultMultiplicity ->
                                Stream.of(ResultTypeComplexity.values()).map(resultTypeComplexity ->
                                        new SyncParamToReturnValueSelectAnalyzer<>(
                                                queryMultiplicity,
                                                queryTypeComplexity,
                                                resultMultiplicity,
                                                resultTypeComplexity
                                        )))
                                .flatMap(identity()))
                        .flatMap(identity()))
                .flatMap(identity());
    }
}
