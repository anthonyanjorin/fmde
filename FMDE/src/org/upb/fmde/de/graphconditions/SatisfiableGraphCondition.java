package org.upb.fmde.de.graphconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public class SatisfiableGraphCondition<Ob, Arr extends ComparableArrow<Arr>> extends GraphCondition<Ob, Arr> {

	public SatisfiableGraphCondition(Category<Ob, Arr> cat, Arr p, List<Arr> ci) {
		super(cat, p, ci);
	}

	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator){
		Ob G = cat.target(m);
		Ob P = cat.target(p);
		
		// TODO (09) Implement the following steps given by Def. 7 
		
		// determine all m_p and filter for commutativity
		
		// determine all m_c_i
		
		// forall m_p: there must be a m_c_i that commutes with it
		
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
