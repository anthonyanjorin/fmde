package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.PatternMatcher;

public class True<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {

	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		// TODO (14) Implement Def 13
	    throw new UnsupportedOperationException("Not implemented yet");
	}

}
