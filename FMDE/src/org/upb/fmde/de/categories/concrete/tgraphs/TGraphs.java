package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism>, 
								CategoryWithInitOb<TGraph, TGraphMorphism>, 
								CategoryWithPushouts<TGraph, TGraphMorphism> {
	private final Graph typeGraph;
	private final TGraph EMPTY_TYPED_GRAPH;
	private final CoLimit<TGraph, TGraphMorphism> INITIAL_OBJECT;
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		this.typeGraph = typeGraph;
		EMPTY_TYPED_GRAPH = new TGraph("EMPTY_TYPED_GRAPH", Graphs.initialObject().up.apply(typeGraph));
		INITIAL_OBJECT = new CoLimit<>(
				EMPTY_TYPED_GRAPH, 
				G -> new TGraphMorphism("initial_" + G.label(), Graphs.initialObject().up.apply(G.type().src()), EMPTY_TYPED_GRAPH, G)
			);
	}
	
	@Override
	public TGraphMorphism compose(TGraphMorphism f, TGraphMorphism g) {
		GraphMorphism f_g = Graphs.compose(f.untyped(), g.untyped());
		return new TGraphMorphism(f_g.label(), f_g, f.src(), g.trg());
	}

	@Override
	public TGraphMorphism id(TGraph f) {
		GraphMorphism id = Graphs.id(f.type().src());
		return new TGraphMorphism(id.label(), id, f, f);
	}
	
	@Override
	public String showOb(TGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.type().trg().label();
	}

	@Override
	public CoLimit<TGraph, TGraphMorphism> initialObject() {
		return INITIAL_OBJECT;
	}

	@Override
	public CoLimit<TGraphMorphism, TGraphMorphism> coequaliser(TGraphMorphism f, TGraphMorphism g) {
		// TODO (7) Calculate componentwise for edges and nodes, use universal properties to induce all necessary arrows  
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> coproduct(TGraph a, TGraph b) {
		// TODO (6) Calculate componentwise for edges and nodes, use universal properties to induce all necessary arrows  
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
