package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import static org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphs.TripleGraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;

public class TypedTripleGraphs implements LabelledCategory<TTripleGraph, TTripleMorphism> {

	public static final TypedTripleGraphs TypedTripleGraphs = new TypedTripleGraphs();

	@Override
	public TTripleMorphism compose(TTripleMorphism f, TTripleMorphism g) {
		TripleMorphism f_g = TripleGraphs.compose(f.getUntypedMorphism(), g.getUntypedMorphism());
		return new TTripleMorphism(f_g.label(), f_g, f.src(), g.trg());
	}

	@Override
	public TTripleMorphism id(TTripleGraph o) {
		return new TTripleMorphism("id_" + o.label(), TripleGraphs.id(o.getTypeMorphism().src()), o, o);
	}
	
	@Override
	public String showOb(TTripleGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.getTypeMorphism().trg().label();
	}
}
