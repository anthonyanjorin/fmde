package org.upb.fmde.de.categories;

public interface CategoryWithInitOb<Ob, Arr> extends Category<Ob, Arr> {
	Ob 	initialObject();
	Arr initialArrowInto(Ob o);
}
