package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.Category;
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
import org.upb.fmde.de.categories.slice.Slice;
import org.upb.fmde.de.categories.slice.Triangle;

public class TGraphs extends    Slice<Graph,GraphMorphism>
                     implements LabelledCategory<GraphMorphism, Triangle<Graph, GraphMorphism>>, 
								CategoryWithInitOb<GraphMorphism, Triangle<Graph, GraphMorphism>>, 
								CategoryWithPushouts<GraphMorphism, Triangle<Graph, GraphMorphism>>,
								CategoryWithPushoutComplements<GraphMorphism, Triangle<Graph, GraphMorphism>> {
	//private final Graph typeGraph;
	private final GraphMorphism EMPTY_TYPED_GRAPH;
	private final CoLimit<GraphMorphism, Triangle<Graph, GraphMorphism>> INITIAL_OBJECT;
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		super(Graphs.Graphs,typeGraph);
		//this.typeGraph = typeGraph;
		EMPTY_TYPED_GRAPH = Graphs.initialObject().up.apply(typeGraph);
		INITIAL_OBJECT = new CoLimit<>(
				EMPTY_TYPED_GRAPH, 
				G -> new Triangle<Graph, GraphMorphism>("initial_" + G.label(), Graphs.initialObject().up.apply(G.src()), EMPTY_TYPED_GRAPH, G)
			);
	}

	@Override
	public CoLimit<GraphMorphism, Triangle<Graph, GraphMorphism>> initialObject() {
		return INITIAL_OBJECT;
	}

	@Override
	public CoLimit<Triangle<Graph, GraphMorphism>, Triangle<Graph, GraphMorphism>> coequaliser(Triangle<Graph, GraphMorphism> f, Triangle<Graph, GraphMorphism> g) {
		CoLimit<GraphMorphism, GraphMorphism> coeq_graphs = Graphs.coequaliser(f.getF(), g.getF());
		GraphMorphism type = coeq_graphs.up.apply(f.trg());
		//TGraph obj = new TGraph("==", type);
		Triangle<Graph, GraphMorphism> coeq_obj = new Triangle<Graph, GraphMorphism>("==", coeq_graphs.obj, f.trg(), type);
		
		return new CoLimit<Triangle<Graph, GraphMorphism>, Triangle<Graph, GraphMorphism>>(
				coeq_obj, 
				tm -> new Triangle<Graph, GraphMorphism>("u", coeq_graphs.up.apply(tm.getF()), type, tm.trg())
				);
	}

	@Override
	public CoLimit<CoSpan<Triangle<Graph, GraphMorphism>>, Triangle<Graph, GraphMorphism>> coproduct(GraphMorphism a, GraphMorphism b) {
		CoLimit<CoSpan<GraphMorphism>, GraphMorphism> coprod_graphs = Graphs.coproduct(a.src(), b.src());
		GraphMorphism type = coprod_graphs.up.apply(new CoSpan<>(Graphs, b, a));
		//GraphMorphism obj = new GraphMorphism(a + "+" + b, type);
		
		return new CoLimit<>(
				new CoSpan<Triangle<Graph, GraphMorphism>>(this, new Triangle<Graph, GraphMorphism>("left", coprod_graphs.obj.left, b, type), 
						     		new Triangle<Graph, GraphMorphism>("right", coprod_graphs.obj.right, a, type)), 
				cos -> new Triangle<Graph, GraphMorphism>("u", 
						coprod_graphs.up.apply(new CoSpan<GraphMorphism>(Graphs, cos.left.getF(), cos.right.getF())),
						type,
						cos.left.trg())
				);
	}

	@Override
	public Optional<Corner<Triangle<Graph, GraphMorphism>>> pushoutComplement(Corner<Triangle<Graph, GraphMorphism>> upperLeft) {
		Triangle<Graph, GraphMorphism> l = upperLeft.first;
		Triangle<Graph, GraphMorphism> m = upperLeft.second;
		GraphMorphism K = l.src();
		GraphMorphism G = m.trg();
		
		return Graphs.pushoutComplement(new Corner<GraphMorphism>(Graphs, l.getF(), m.getF()))
				.map(pc_G -> determinedTypedPushoutComplement(pc_G, K, G));
	}

	private Corner<Triangle<Graph, GraphMorphism>> determinedTypedPushoutComplement(Corner<GraphMorphism> pc_G, GraphMorphism K, GraphMorphism G) {
		Graph D = pc_G.first.trg();
		
		TotalFunction type_E = new TotalFunction(D.edges(), "type_E", T.edges());
		D.edges().elts().forEach(e -> type_E.addMapping(e, G._E().map(e)));
		
		TotalFunction type_V = new TotalFunction(D.vertices(), "type_V", T.vertices());
		D.vertices().elts().forEach(v -> type_V.addMapping(v, G._V().map(v)));
		
		GraphMorphism type_D = new GraphMorphism("type_D", D, T, type_E, type_V);
		//TGraph D_ = new TGraph("D", type_D);
		
		Triangle<Graph, GraphMorphism> d = new Triangle<Graph, GraphMorphism>("d", pc_G.first, K, type_D);
		Triangle<Graph, GraphMorphism> l_ = new Triangle<Graph, GraphMorphism>("l'", pc_G.second, type_D, G);
		
		return new Corner<>(this, d, l_);
	}

	@Override
	public Corner<Triangle<Graph, GraphMorphism>> restrict(Corner<Triangle<Graph, GraphMorphism>> upperLeft) {
		Corner<GraphMorphism> untyped = Graphs.restrict(new Corner<>(Graphs.Graphs, source(upperLeft.first), source(upperLeft.second)));
		
		Triangle<Graph, GraphMorphism> G = upperLeft.second;
		Graph G_ = untyped.first.trg();
		
		TotalFunction type_G_E = new TotalFunction(G_.edges(), "type_G_E", T.edges());
		G_.edges().elts().forEach(e -> type_G_E.addMapping(e, G.trg()._E().map(e)));
		
		TotalFunction type_G_V = new TotalFunction(G_.vertices(), "type_G_V", T.vertices());
		G_.vertices().elts().forEach(v -> type_G_V.addMapping(v, G.trg()._V().map(v)));
		
		GraphMorphism type_G = new GraphMorphism("type_G", G_, T, type_G_E, type_G_V);
		//GraphMorphism _G_ = new GraphMorphism("G_", type_G);
		
		return new Corner<>(this, new Triangle<Graph, GraphMorphism>("_m_", untyped.first, upperLeft.first.trg(), type_G),
								  new Triangle<Graph, GraphMorphism>("_g_", untyped.second, G.getType(), G.getType_()));
	}
}
