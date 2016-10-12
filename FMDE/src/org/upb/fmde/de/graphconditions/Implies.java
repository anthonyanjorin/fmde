package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.PatternMatcher;

public class Implies<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {

	private ComplexGraphCondition<Ob, Arr> innerCondition;
	
	public Implies(ComplexGraphCondition<Ob, Arr> premise, ComplexGraphCondition<Ob, Arr> conclusion){
		// TODO (18) Implement Def 12
	    throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		return innerCondition.isSatisfiedByArrow(m, creator);
	}
	
}
