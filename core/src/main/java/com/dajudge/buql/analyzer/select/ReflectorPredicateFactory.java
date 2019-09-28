package com.dajudge.buql.analyzer.select;

import com.dajudge.buql.reflector.predicate.ReflectorPredicate;

public interface ReflectorPredicateFactory {
    ReflectorPredicate createPredicate(final Class<?> actualQueryType, final String predicateName);
}
