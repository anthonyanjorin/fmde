package org.upb.fmde.de.categories.typedtriplegraphs;

import static org.upb.fmde.de.categories.triplegraphs.TripleGraphs.TripleGraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.triplegraphs.TripleMorphism;

public class TypedTripleGraphs implements LabelledCategory<TTripleGraph, TTripleMorphism> {

	public static final TypedTripleGraphs TypedTripleGraphs = new TypedTripleGraphs();

	@Override
	public TTripleMorphism compose(TTripleMorphism f, TTripleMorphism g) {
		TripleMorphism f_g = TripleGraphs.compose(f.getUntypedMorphism(), g.getUntypedMorphism());
		return new TTripleMorphism(f_g.label(), f_g, f.getSource(), g.getTarget());
	}

	@Override
	public TTripleMorphism id(TTripleGraph o) {
		return new TTripleMorphism("id_" + o.label(), TripleGraphs.id(o.getTypeMorphism().getSource()), o, o);
	}
	
	@Override
	public String showOb(TTripleGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.getTypeMorphism().getTarget().label();
	}
}
