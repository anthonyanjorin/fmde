package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class Span<Arr> {
	public final Arr left;
	public final Arr right;
	
	public Span(Category<?, Arr> cat, Arr left, Arr right) {
		this(left, right);
		Category.ensure(cat.source(left).equals(cat.source(right)), "This is not a span!");
	}
	
	protected Span(Arr left, Arr right){
		this.left = left;
		this.right = right;
	}
}