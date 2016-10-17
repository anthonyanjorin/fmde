package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class CoSpan<Arr> extends Span<Arr>{
	public CoSpan(Category<?, Arr> cat, Arr left, Arr right) {
		super(left, right);
		Category.ensure(cat.target(left).equals(cat.target(right)), 
				"This is not a co-span! " + cat.target(left) + " != " + cat.target(right));
	}
}