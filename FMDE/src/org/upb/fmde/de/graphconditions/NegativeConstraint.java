package org.upb.fmde.de.graphconditions;

import java.util.Collections;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.ComparableArrow;

public class NegativeConstraint<Ob, Arr extends ComparableArrow<Arr>> extends Constraint<Ob, Arr>{

	public NegativeConstraint(CategoryWithInitOb<Ob, Arr> cat, Ob N) {
		super(cat, N, Collections.<Arr>emptyList());
	}
}
