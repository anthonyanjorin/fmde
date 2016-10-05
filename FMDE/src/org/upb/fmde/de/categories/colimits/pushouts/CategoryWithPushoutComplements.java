package org.upb.fmde.de.categories.colimits.pushouts;

import java.util.Optional;

public interface CategoryWithPushoutComplements<Ob, Arr> extends CategoryWithPushouts<Ob, Arr> {
	
	Optional<Corner<Arr>> pushoutComplement(Corner<Arr> upperLeft);
	
	default Optional<DirectDerivation<Arr>> doublePushout(Span<Arr> L_K_R, Arr match){
		Corner<Arr> upperLeftCorner = new Corner<>(L_K_R.vert, match);
		
		return pushoutComplement(upperLeftCorner).map(firstBottomRightCorner -> {
			CoSpan<Arr> secondBottomRightCorner = pushout(new Span<>(L_K_R.horiz, firstBottomRightCorner.first)).obj;
			return new DirectDerivation<>(firstBottomRightCorner, secondBottomRightCorner);
		});
	}
}
