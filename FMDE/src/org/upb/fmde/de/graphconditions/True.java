package org.upb.fmde.de.graphconditions;

import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.PatternMatcher;

public class True<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {

	@Override
	public boolean isSatisfiedByArrow(Arr m, @NonNull BiFunction<Ob, Ob, @NonNull PatternMatcher<Ob, Arr>> creator) {
		return true;
	}

}
