package org.upb.fmde.de.categories.concrete.tgraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.graphs.Graph;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism> {
	private final Graph typeGraph;
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		this.typeGraph = typeGraph;
	}
	
	@Override
	public TGraphMorphism compose(TGraphMorphism f, TGraphMorphism g) {
		// TODO (07) Composition of typed graph morphisms
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	@Override
	public TGraphMorphism id(TGraph f) {
		// TODO (08) id typed graph morphisms
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}
	
	@Override
	public String showOb(TGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.type().trg().label();
	}
}
