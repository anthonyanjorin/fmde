package org.upb.fmde.de.categories.pushouts;

import org.upb.fmde.de.categories.Category;

public class Span<Arr> {
	public final Arr horizontal;
	public final Arr vertical;
	
	public Span(Category<?, Arr> cat, Arr horizontal, Arr vertical) {
		this(horizontal, vertical);
		Category.ensure(cat.source(horizontal).equals(cat.source(vertical)), "This is not a span!");
	}
	
	protected Span(Arr horizontal, Arr vertical){
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
}