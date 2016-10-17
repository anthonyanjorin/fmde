package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.colimits.CoLimit;

public interface CategoryWithPushouts<Ob, Arr> extends Category<Ob, Arr> {
	
	default CoLimit<CoSpan<Arr>, Arr> pushout(Span<Arr> span) {
		// TODO (1) Implement generic pushout construction based on coequalisers
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	CoLimit<Arr, Arr> coequaliser(Arr f, Arr g);
	
	CoLimit<CoSpan<Arr>, Arr> coproduct(Ob a, Ob b);
}
