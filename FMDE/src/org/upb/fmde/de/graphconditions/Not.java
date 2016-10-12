package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.PatternMatcher;

public class Not<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {

	private ComplexGraphCondition<Ob, Arr> negatedCondition;
	
	public Not(ComplexGraphCondition<Ob, Arr> negatedCondition) {
		this.negatedCondition = negatedCondition;
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		// TODO (15) Implement Def 13
	    throw new UnsupportedOperationException("Not implemented yet");
	}

}
