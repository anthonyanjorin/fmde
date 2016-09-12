package org.upb.fmde.de.graphconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.PatternMatcher;

public abstract class GraphCondition<Ob, Arr> implements ComplexGraphCondition<Ob, Arr> {
	protected Category<Ob, Arr> cat;
	protected Arr p;
	protected List<Arr> ci;

	public GraphCondition(Category<Ob, Arr> cat, Arr p, List<? extends Arr> ci) {
		this.cat = cat;
		this.p = p;
		this.ci = new ArrayList<>();
		this.ci.addAll(ci);
		ensureValidity();
	}

	private void ensureValidity() {
		ci.forEach(c -> {
			if (!cat.target(p).equals(cat.source(c)))
				throw new IllegalStateException("Graph condition is not well-formed!");
		});
	}

	@Override
	public abstract boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator);
}
