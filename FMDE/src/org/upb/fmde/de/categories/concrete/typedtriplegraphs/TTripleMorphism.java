/*package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import static org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphs.TripleGraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.slice.Triangle;

public class TTripleMorphism extends LabelledArrow<Triangle<TripleGraph, TripleMorphism>> implements ComparableArrow<Triangle<TripleGraph, TripleMorphism>> {

	private TripleMorphism f;
	
	public TTripleMorphism(String label, TripleMorphism f, Triangle<TripleGraph, TripleMorphism> source, Triangle<TripleGraph, TripleMorphism> target) {
		super(label, source, target);
		this.f = f;
		if(!isValid()) throw new IllegalArgumentException("Typed TripleMorphism " + label + " is not valid.");
	}

	private boolean isValid() {
		return TripleGraphs.compose(f, target.getType())
				     .isTheSameAs(source.getType());
	}

	public TripleMorphism getUntypedMorphism(){
		return f;
	}
	
	@Override
	public boolean isTheSameAs(Triangle<TripleGraph, TripleMorphism> a) {
		return source.getType().isTheSameAs(a.getType()) &&
			   target.getType().isTheSameAs(a.getType_()) &&
			   f.isTheSameAs(a.getF());
	}

}*/
