package org.upb.fmde.de.graphconditions;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public class SatisfiableGraphCondition<Ob, Arr extends ComparableArrow<Arr>> extends GraphCondition<Ob, Arr> {

	public SatisfiableGraphCondition(Category<Ob, Arr> cat, Arr p, List<Arr> ci) {
		super(cat, p, ci);
	}

	public boolean isSatisfiedBy(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator){
		Ob G = cat.target(m);
		Ob P = cat.target(p);
		
		// determine all m_p and filter for commutativity
		PatternMatcher<Ob, Arr> premise = creator.apply(P, G);
		Stream<Arr> m_pi = premise.getMonicMatches()
				.stream()
				.filter(mp -> m.isTheSameAs(cat.compose(p, mp)));
		
		// determine all m_c_i
		List<Arr> m_ci = ci
				.stream()
				.flatMap(c -> {
					PatternMatcher<Ob, Arr> conclusion = creator.apply(cat.target(c), G);
					return conclusion.getMonicMatches().stream();
				})
				.collect(Collectors.toList());
		
		// forall m_p: there must be a m_c_i that commutes with it
		return m_pi.allMatch(m_p -> m_ci
				.stream()
				.anyMatch(m_c -> m_p.isTheSameAs(cat.compose(ci.get(m_ci.indexOf(m_c)), m_c)))
				);
	}
}
