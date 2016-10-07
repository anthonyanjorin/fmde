package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;

public class TTripleMorphism extends LabelledArrow<TTripleGraph> implements ComparableArrow<TTripleMorphism> {

	private TripleMorphism f;
	
	public TTripleMorphism(String label, TripleMorphism f, TTripleGraph source, TTripleGraph target) {
		super(label, source, target);
		this.f = f;
		if(!isValid()) throw new IllegalArgumentException("Typed TripleMorphism " + label + " is not valid.");
	}

	private boolean isValid() {
		// TODO (13) Structure preservation
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	public TripleMorphism getUntypedMorphism(){
		return f;
	}
	
	@Override
	public boolean isTheSameAs(TTripleMorphism a) {
		// TODO (14) Equality
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

}
