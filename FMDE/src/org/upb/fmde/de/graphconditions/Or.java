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
		Collection<ComplexGraphCondition<Ob, Arr>> negatedConditions = conditions
				.stream()
				.map(c -> new Not<>(c))
				.collect(Collectors.toList());
		
		innerCondition = new Not<>(new And<>(negatedConditions));
	}

	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		return innerCondition.isSatisfiedByArrow(m, creator);
	}

}
