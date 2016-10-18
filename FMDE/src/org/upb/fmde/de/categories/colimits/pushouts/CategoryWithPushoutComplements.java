package org.upb.fmde.de.categories.colimits.pushouts;

import java.util.Optional;

public interface CategoryWithPushoutComplements<Ob, Arr> extends CategoryWithPushouts<Ob, Arr> {
	
	Optional<Corner<Arr>> pushoutComplement(Corner<Arr> upperLeft);
	
	default Corner<Arr> restrict(Corner<Arr> upperLeft){
		return upperLeft;
	}
	
	default Optional<DirectDerivation<Arr>> doublePushout(Span<Arr> L_K_R, Arr match){
		Corner<Arr> upperLeftCorner = new Corner<>(L_K_R.left, match);
		
		// TODO (1) Implement double pushout construction
		
		// Determine first bottom right corner as a pushout complement
		
		// Determine second bottom right corner as a pushout
		
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
