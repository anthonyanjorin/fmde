package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class CoSpan<Arr> extends Span<Arr>{
	public CoSpan(Category<?, Arr> cat, Arr horizontal, Arr vertical) {
		super(horizontal, vertical);
		Category.ensure(cat.target(horizontal).equals(cat.target(vertical)), 
				"This is not a co-span! " + cat.target(horizontal) + " != " + cat.target(vertical));
	}
}