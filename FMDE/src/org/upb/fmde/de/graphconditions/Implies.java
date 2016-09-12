package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.PatternMatcher;

public class Implies<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {

	private ComplexGraphCondition<Ob, Arr> innerCondition;
	
	public Implies(ComplexGraphCondition<Ob, Arr> premise, ComplexGraphCondition<Ob, Arr> conclusion){
		innerCondition = new Or<>(new Not<>(premise), conclusion);
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		return innerCondition.isSatisfiedByArrow(m, creator);
	}
	
}
