package org.upb.fmde.de.graphconditions;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.upb.fmde.de.categories.PatternMatcher;

public class Or<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {
	private ComplexGraphCondition<Ob, Arr> innerCondition;

	@SafeVarargs
	public Or(ComplexGraphCondition<Ob, Arr>...conditions){
		this(Arrays.asList(conditions));
	}
	
	public Or(Collection<ComplexGraphCondition<Ob, Arr>> conditions) {
		// TODO (19) Implement Def 12
	    throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		return innerCondition.isSatisfiedByArrow(m, creator);
	}

}
