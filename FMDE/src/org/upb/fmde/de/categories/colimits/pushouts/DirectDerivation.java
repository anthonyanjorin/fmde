package org.upb.fmde.de.categories.colimits.pushouts;

import org.upb.fmde.de.categories.Category;

public class DirectDerivation<Arr> {
	public final Corner<Arr> pushoutComplement;
	public final CoSpan<Arr> pushout;
	
	public DirectDerivation(Category<?, Arr> cat, Corner<Arr> pushoutComplement, CoSpan<Arr> pushout) {
		this(pushoutComplement, pushout);
		Category.ensure(cat.target(pushoutComplement.first).equals(cat.source(pushout.horiz)), "This is not a direct derivation!");
	}
	
	protected DirectDerivation(Corner<Arr> pushoutComplement, CoSpan<Arr> pushout){
		this.pushoutComplement = pushoutComplement;
		this.pushout = pushout;
	}
}
