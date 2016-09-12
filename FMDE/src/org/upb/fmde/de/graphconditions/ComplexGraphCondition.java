package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.PatternMatcher;

public interface ComplexGraphCondition<Ob, Arr> {
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator);
}
