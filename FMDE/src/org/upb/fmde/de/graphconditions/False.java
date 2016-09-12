package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.PatternMatcher;

public class False<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {
	private ComplexGraphCondition<Ob, Arr> innerCondition;

	public False() {
		innerCondition = new Not<>(new True<>());
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		return innerCondition.isSatisfiedByArrow(m, creator);
	}
	
	
}
