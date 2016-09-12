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
		
		// determine all m_p and filter for commutativity
		PatternMatcher<Ob, Arr> premise = creator.apply(P, G);
		Stream<Arr> m_pi = premise.getMonicMatches()
				.stream()
				.filter(mp -> m.isTheSameAs(cat.compose(p, mp)));
		
		// determine all m_c_i
		Map<Arr, Arr> m_ci = new HashMap<>();
		ci.stream()
		  .forEach(c -> {
				PatternMatcher<Ob, Arr> conclusion = creator.apply(cat.target(c), G);
				conclusion.getMonicMatches().forEach(m_c -> m_ci.put(m_c, c));
		  });
		
		// forall m_p: there must be a m_c_i that commutes with it
		return m_pi.allMatch(m_p -> m_ci.keySet()
				.stream()
				.anyMatch(m_c -> m_p.isTheSameAs(cat.compose(m_ci.get(m_c), m_c)))
				);
	}
}
