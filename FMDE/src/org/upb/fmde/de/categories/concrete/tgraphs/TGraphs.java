package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushoutComplements;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism>, 
								CategoryWithInitOb<TGraph, TGraphMorphism>, 
								CategoryWithPushouts<TGraph, TGraphMorphism>,
								CategoryWithPushoutComplements<TGraph, TGraphMorphism> {
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
		CoLimit<GraphMorphism, GraphMorphism> coeq_graphs = Graphs.coequaliser(f.untyped(), g.untyped());
		GraphMorphism type = coeq_graphs.up.apply(f.trg().type());
		TGraph obj = new TGraph("==", type);
		TGraphMorphism coeq_obj = new TGraphMorphism("==", coeq_graphs.obj, f.trg(), obj);
		
		return new CoLimit<TGraphMorphism, TGraphMorphism>(
				coeq_obj, 
				tm -> new TGraphMorphism("u", coeq_graphs.up.apply(tm.untyped()), obj, tm.trg())
				);
	}

	@Override
	public CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> coproduct(TGraph a, TGraph b) {
		CoLimit<CoSpan<GraphMorphism>, GraphMorphism> coprod_graphs = Graphs.coproduct(a.type().src(), b.type().src());
		GraphMorphism type = coprod_graphs.up.apply(new CoSpan<>(Graphs, b.type(), a.type()));
		TGraph obj = new TGraph(a + "+" + b, type);
		
		return new CoLimit<>(
				new CoSpan<>(this, new TGraphMorphism("horiz", coprod_graphs.obj.horiz, b, obj), 
						     		new TGraphMorphism("vert", coprod_graphs.obj.vert, a, obj)), 
				cos -> new TGraphMorphism("u", 
						coprod_graphs.up.apply(new CoSpan<>(Graphs, cos.horiz.untyped(), cos.vert.untyped())),
						obj,
						cos.horiz.trg())
				);
	}

	@Override
	public Optional<Corner<TGraphMorphism>> pushoutComplement(Corner<TGraphMorphism> upperLeft) {
		TGraphMorphism l = upperLeft.first;
		TGraphMorphism m = upperLeft.second;
		TGraph K = l.src();
		TGraph G = m.trg();
		
		return Graphs.pushoutComplement(new Corner<>(Graphs, l.untyped(), m.untyped()))
				.map(pc_G -> determinedTypedPushoutComplement(pc_G, K, G));
	}

	private Corner<TGraphMorphism> determinedTypedPushoutComplement(Corner<GraphMorphism> pc_G, TGraph K, TGraph G) {
		Graph D = pc_G.first.trg();
		
		TotalFunction type_E = new TotalFunction(D.edges(), "type_E", typeGraph.edges());
		D.edges().elts().forEach(e -> type_E.addMapping(e, G.type()._E().map(e)));
		
		TotalFunction type_V = new TotalFunction(D.vertices(), "type_V", typeGraph.vertices());
		D.vertices().elts().forEach(v -> type_V.addMapping(v, G.type()._V().map(v)));
		
		GraphMorphism type_D = new GraphMorphism("type_D", D, typeGraph, type_E, type_V);
		TGraph D_ = new TGraph("D", type_D);
		
		TGraphMorphism d = new TGraphMorphism("d", pc_G.first, K, D_);
		TGraphMorphism l_ = new TGraphMorphism("l'", pc_G.second, D_, G);
		
		return new Corner<>(this, d, l_);
	}

	@Override
	public Corner<TGraphMorphism> restrict(Corner<TGraphMorphism> upperLeft) {
		Corner<GraphMorphism> untyped = Graphs.restrict(new Corner<>(Graphs.Graphs, upperLeft.first.untyped(), upperLeft.second.untyped()));
		
		TGraph G = upperLeft.second.trg();
		Graph G_ = untyped.first.trg();
		
		TotalFunction type_G_E = new TotalFunction(G_.edges(), "type_G_E", typeGraph.edges());
		G_.edges().elts().forEach(e -> type_G_E.addMapping(e, G.type()._E().map(e)));
		
		TotalFunction type_G_V = new TotalFunction(G_.vertices(), "type_G_V", typeGraph.vertices());
		G_.vertices().elts().forEach(v -> type_G_V.addMapping(v, G.type()._V().map(v)));
		
		GraphMorphism type_G = new GraphMorphism("type_G", G_, typeGraph, type_G_E, type_G_V);
		TGraph _G_ = new TGraph("G_", type_G);
		
		return new Corner<>(this, new TGraphMorphism("_m_", untyped.first, upperLeft.first.trg(), _G_),
								  new TGraphMorphism("_g_", untyped.second, _G_, G));
	}
}
