package org.upb.fmde.de.categories.tgraphs;

import static org.upb.fmde.de.categories.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphMorphism;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism>, CategoryWithInitOb<TGraph, TGraphMorphism> {
	private final Graph typeGraph;
	private final TGraph EMPTY_TYPED_GRAPH;
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		this.typeGraph = typeGraph;
		EMPTY_TYPED_GRAPH = new TGraph("EMPTY_TYPED_GRAPH", Graphs.initialArrowInto(typeGraph));
	}
	
	@Override
	public TGraphMorphism compose(TGraphMorphism f, TGraphMorphism g) {
		GraphMorphism f_g = Graphs.compose(f.getUntypedMorphism(), g.getUntypedMorphism());
		return new TGraphMorphism(f_g.label(), f_g, f.getSource(), g.getTarget());
	}

	@Override
	public TGraphMorphism id(TGraph f) {
		GraphMorphism id = Graphs.id(f.type().getSource());
		return new TGraphMorphism(id.label(), id, f, f);
	}
	
	@Override
	public String showOb(TGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.type().getTarget().label();
	}

	@Override
	public TGraph initialObject() {
		return EMPTY_TYPED_GRAPH;
	}

	@Override
	public TGraphMorphism initialArrowInto(TGraph o) {
		return new TGraphMorphism("initial_" + o.label(), Graphs.initialArrowInto(o.type().getSource()), EMPTY_TYPED_GRAPH, o);
	}
}
