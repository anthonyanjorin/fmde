package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class Corner<Arr> {

	public final Arr first;
	public final Arr second;
	
	public Corner(Category<?, Arr> cat, Arr first, Arr second) {
		this(first, second);
		Category.ensure(cat.target(first).equals(cat.source(second)), "This is not a corner!");
	}
	
	protected Corner(Arr first, Arr second){
		this.first = first;
		this.second = second;
	}
}
