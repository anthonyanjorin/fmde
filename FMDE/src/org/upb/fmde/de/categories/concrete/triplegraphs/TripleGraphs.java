package org.upb.fmde.de.categories.concrete.triplegraphs;

import org.upb.fmde.de.categories.LabelledCategory;

public class TripleGraphs implements LabelledCategory<TripleGraph, TripleMorphism> {

	public static TripleGraphs TripleGraphs = new TripleGraphs();
	
	@Override
	public TripleMorphism compose(TripleMorphism f, TripleMorphism g) {
		// TODO (09) Composition
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	@Override
	public TripleMorphism id(TripleGraph f) {
		// TODO (10) id
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}
}
