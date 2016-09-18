package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import static org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphs.TripleGraphs;

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
		return TripleGraphs.compose(f, target.getTypeMorphism())
				     .isTheSameAs(source.getTypeMorphism());
	}

	public TripleMorphism getUntypedMorphism(){
		return f;
	}
	
	@Override
	public boolean isTheSameAs(TTripleMorphism a) {
		return source.getTypeMorphism().isTheSameAs(a.source.getTypeMorphism()) &&
			   target.getTypeMorphism().isTheSameAs(a.trg().getTypeMorphism()) &&
			   f.isTheSameAs(a.f);
	}

}
