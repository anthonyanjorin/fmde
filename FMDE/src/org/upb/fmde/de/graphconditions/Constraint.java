package org.upb.fmde.de.graphconditions;

import java.util.List;
import java.util.function.BiFunction;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;

public class Constraint<Ob, Arr extends ComparableArrow<Arr>> extends SatisfiableGraphCondition<Ob, Arr> {

	private CategoryWithInitOb<Ob, Arr> catWithInitOb;
	
	public Constraint(CategoryWithInitOb<Ob, Arr> cat, Ob P, List<Arr> ci) {
		super(cat, cat.initialObject().up.apply(P), ci);
		catWithInitOb = cat;
	}

	public boolean isSatisfiedByObject(Ob o, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		// TODO (13) Simplify Def 7 appropriately
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
