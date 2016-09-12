package org.upb.fmde.de.graphconditions;

import java.util.Arrays;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.ComparableArrow;

public class SimpleConstraint<Ob, Arr extends ComparableArrow<Arr>> extends Constraint<Ob, Arr> {
	public SimpleConstraint(CategoryWithInitOb<Ob, Arr> cat, Ob C) {
		super(cat, cat.initialObject(), Arrays.asList(cat.initialArrowInto(C)));
	}
}
