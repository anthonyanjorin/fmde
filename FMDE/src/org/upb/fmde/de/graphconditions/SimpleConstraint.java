package org.upb.fmde.de.graphconditions;

import java.util.Arrays;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;

public class SimpleConstraint<Ob, Arr extends ComparableArrow<Arr>> extends Constraint<Ob, Arr> {
	public SimpleConstraint(CategoryWithInitOb<Ob, Arr> cat, Ob C) {
		super(cat, cat.initialObject().obj, Arrays.asList(cat.initialObject().up.apply(C)));
	}
}
