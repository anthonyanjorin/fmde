package org.upb.fmde.de.categories.colimits;

import java.util.function.Function;

public class CoLimit<O, A> {
	public final O obj;
	public final Function<O, A> up;
	
	public CoLimit(O colimitObj, Function<O, A> univProp) {
		this.obj = colimitObj;
		this.up = univProp;
	}
}
