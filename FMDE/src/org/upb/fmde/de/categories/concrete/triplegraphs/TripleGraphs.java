package org.upb.fmde.de.categories.concrete.triplegraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.LabelledCategory;

public class TripleGraphs implements LabelledCategory<TripleGraph, TripleMorphism> {

	public static TripleGraphs TripleGraphs = new TripleGraphs();
	
	@Override
	public TripleMorphism compose(TripleMorphism f, TripleMorphism g) {
		return new TripleMorphism(f.label() + ";" + g.label(), f.src(), g.trg(), 
				Graphs.compose(f.getF_S(), g.getF_S()), 
				Graphs.compose(f.getF_C(), g.getF_C()), 
				Graphs.compose(f.getF_T(), g.getF_T()));
	}

	@Override
	public TripleMorphism id(TripleGraph f) {
		return new TripleMorphism("id_" + f.label(), f, f,
				Graphs.id(f.getG_S()), 
				Graphs.id(f.getG_C()), 
				Graphs.id(f.getG_T()));
	}
}
