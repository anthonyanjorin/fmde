package org.upb.fmde.de.categories.tgraphs;

import static org.upb.fmde.de.categories.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.graphs.GraphMorphism;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism> {

	public static TGraphs TGraphs = new TGraphs();
	
	@Override
	public TGraphMorphism compose(TGraphMorphism f, TGraphMorphism g) {
		GraphMorphism f_g = Graphs.compose(f.getUntypedMorphism(), g.getUntypedMorphism());
		return new TGraphMorphism(f_g.label(), f_g, f.getSource(), g.getTarget());
	}

	@Override
	public TGraphMorphism id(TGraph f) {
		GraphMorphism id = Graphs.id(f.getTypeMorphism().getSource());
		return new TGraphMorphism(id.label(), id, f, f);
	}
	
	@Override
	public String showOb(TGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.getTypeMorphism().getTarget().label();
	}
}
