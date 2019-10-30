package com.dajudge.buql.analyzer.api;

import java.util.stream.Stream;

public interface AnalyzerFactory {
    Stream<Analyzer> create();
}
