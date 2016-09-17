package org.upb.fmde.de.categories.pushouts;

import org.upb.fmde.de.categories.Category;

public interface CategoryWithPushouts<Ob, Arr> extends Category<Ob, Arr> {
	
	default CoSpan<Arr> pushout(Span<Arr> span) {
		CoSpan<Arr> G_Plus_R = coproduct(span);
		
		CoSpan<Arr> cospan = new CoSpan<>(this, 
				compose(span.horizontal, G_Plus_R.vertical), 
				compose(span.vertical, G_Plus_R.horizontal));
		
		Arr coequaliser = coequaliser(cospan);
		
		return new CoSpan<Arr>(this,
				compose(G_Plus_R.horizontal, coequaliser),
				compose(G_Plus_R.vertical, coequaliser));
	}
	
	Arr coequaliser(CoSpan<Arr> cospan);
	
	CoSpan<Arr> coproduct(Span<Arr> span);

	Arr universalProperty(CoSpan<Arr> pushout, CoSpan<Arr> cospan);
}
