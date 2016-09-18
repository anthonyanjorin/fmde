package org.upb.fmde.de.categories.colimits;

import org.upb.fmde.de.categories.Category;

public interface CategoryWithInitOb<Ob, Arr> extends Category<Ob, Arr> {
	CoLimit<Ob, Arr> initialObject();
}
