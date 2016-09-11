package org.upb.fmde.de.graphconditions;

import java.util.List;

import org.upb.fmde.de.categories.Category;

public class Constraint<Ob, Arr> extends GraphCondition<Ob, Arr> {

	public Constraint(Category<Ob, Arr> cat, Arr p, List<Arr> ci) {
		super(cat, p, ci);
	}

}
