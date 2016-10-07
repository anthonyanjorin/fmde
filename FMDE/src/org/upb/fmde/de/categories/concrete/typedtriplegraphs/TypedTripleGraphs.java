package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import org.upb.fmde.de.categories.LabelledCategory;

public class TypedTripleGraphs implements LabelledCategory<TTripleGraph, TTripleMorphism> {

	public static final TypedTripleGraphs TypedTripleGraphs = new TypedTripleGraphs();

	@Override
	public TTripleMorphism compose(TTripleMorphism f, TTripleMorphism g) {
		// TODO (15) Composition
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	@Override
	public TTripleMorphism id(TTripleGraph o) {
		// TODO (16) id
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}
	
	@Override
	public String showOb(TTripleGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.getTypeMorphism().trg().label();
	}
}
