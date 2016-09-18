package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class Span<Arr> {
	public final Arr horiz;
	public final Arr vert;
	
	public Span(Category<?, Arr> cat, Arr horizontal, Arr vertical) {
		this(horizontal, vertical);
		Category.ensure(cat.source(horizontal).equals(cat.source(vertical)), "This is not a span!");
	}
	
	protected Span(Arr horizontal, Arr vertical){
		this.horiz = horizontal;
		this.vert = vertical;
	}
}