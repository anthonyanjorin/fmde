package org.upb.fmde.de.categories.concrete.graphs;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushoutComplements;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

public class Graphs implements LabelledCategory<Graph, GraphMorphism>, 
							   CategoryWithInitOb<Graph, GraphMorphism>, 
							   CategoryWithPushouts<Graph, GraphMorphism>,
							   CategoryWithPushoutComplements<Graph, GraphMorphism> {

	public static Graphs Graphs = new Graphs();
	
	private final FinSet emptySet = FinSets.initialObject().obj;
	private final TotalFunction emptySetArrow = FinSets.initialObject().up.apply(emptySet);
	private final Graph EMPTY_GRAPH = new Graph("EMPTY_GRAPH", emptySet, emptySet, emptySetArrow, emptySetArrow);
	private final CoLimit<Graph, GraphMorphism> INITIAL_OBJECT = 
			new CoLimit<>(
					EMPTY_GRAPH,
					G ->  new GraphMorphism("initial_" + G.label(), EMPTY_GRAPH, G, 
					FinSets.initialObject().up.apply(G.edges()), 
					FinSets.initialObject().up.apply(G.vertices()))
			);
	
	@Override
	public GraphMorphism compose(GraphMorphism f, GraphMorphism g) {
		return new GraphMorphism(
			f.label() + ";" + g.label(),
			f.src(),
			g.trg(),
			FinSets.compose(f._E(), g._E()),
			FinSets.compose(f._V(), g._V())
		);
	}

	@Override
	public GraphMorphism id(Graph g) {
		return new GraphMorphism("id_" + g.label(), g, g, FinSets.id(g.edges()), FinSets.id(g.vertices()));
	}

	@Override
	public CoLimit<Graph, GraphMorphism> initialObject() {
		return INITIAL_OBJECT;
	}

	@Override
	public CoLimit<GraphMorphism, GraphMorphism> coequaliser(GraphMorphism f, GraphMorphism g) {
		CoLimit<TotalFunction, TotalFunction> coeqEdges = FinSets.coequaliser(f._E(), g._E());
		CoLimit<TotalFunction, TotalFunction> coeqVertices = FinSets.coequaliser(f._V(), g._V());
		
		TotalFunction u_s = coeqEdges.up.apply(FinSets.compose(f.trg().src(), coeqVertices.obj));
		TotalFunction u_t = coeqEdges.up.apply(FinSets.compose(f.trg().trg(), coeqVertices.obj));
		
		Graph obj = new Graph(f.label() + "==" + g.label(), coeqEdges.obj.trg(), coeqVertices.obj.trg(), u_s, u_t);
		return new CoLimit<>(
			new GraphMorphism(obj.label(), f.trg(), obj, coeqEdges.obj, coeqVertices.obj),
			e -> {
				TotalFunction u_E = coeqEdges.up.apply(e._E());
				TotalFunction u_V = coeqVertices.up.apply(e._V());
				return new GraphMorphism("u", obj, e.trg(), u_E, u_V);
			});
	}

	@Override
	public CoLimit<CoSpan<GraphMorphism>, GraphMorphism> coproduct(Graph a, Graph b) {
		CoLimit<CoSpan<TotalFunction>, TotalFunction> coprodEdges = FinSets.coproduct(a.edges(), b.edges());
		CoLimit<CoSpan<TotalFunction>, TotalFunction> coprodVertices = FinSets.coproduct(a.vertices(), b.vertices());
		
		TotalFunction left_s = FinSets.compose(b.src(), coprodVertices.obj.left);
		TotalFunction right_s = FinSets.compose(a.src(), coprodVertices.obj.right);
		TotalFunction u_s = coprodEdges.up.apply(new CoSpan<TotalFunction>(FinSets, left_s, right_s));
		
		TotalFunction left_t = FinSets.compose(b.trg(), coprodVertices.obj.left);
		TotalFunction right_t = FinSets.compose(a.trg(), coprodVertices.obj.right);
		TotalFunction u_t = coprodEdges.up.apply(new CoSpan<TotalFunction>(FinSets, left_t, right_t));
		
		Graph obj = new Graph(a.label() + " + " + b.label(), coprodEdges.obj.left.trg(), coprodVertices.obj.left.trg(), u_s, u_t);
		GraphMorphism left = new GraphMorphism("left", b, obj, coprodEdges.obj.left, coprodVertices.obj.left);
		GraphMorphism right = new GraphMorphism("right", a, obj, coprodEdges.obj.right, coprodVertices.obj.right);
		return new CoLimit<>(
				new CoSpan<>(this, left, right), 
				cos -> {
					TotalFunction u_E = coprodEdges.up.apply(new CoSpan<>(FinSets, cos.left._E(), cos.right._E()));
					TotalFunction u_V = coprodVertices.up.apply(new CoSpan<>(FinSets, cos.left._V(), cos.right._V()));
					return new GraphMorphism("u", obj, cos.left.trg(), u_E, u_V);
				});
	}

	@SuppressWarnings("null")
	@Override
	public Optional<Corner<GraphMorphism>> pushoutComplement(Corner<GraphMorphism> upperLeft) {
		if(!determineDanglingEdges(upperLeft).isEmpty())
			return Optional.empty();
		
		GraphMorphism l = upperLeft.first;
		GraphMorphism m = upperLeft.second;
		Graph K = l.src();
		Graph G = m.trg();

		return FinSets.pushoutComplement(new Corner<>(FinSets, l._V(), m._V()))
				.flatMap(pc_V -> FinSets.pushoutComplement(new Corner<>(FinSets, l._E(), m._E()))
				.map(pc_E -> determinePushOutComplement(pc_V, pc_E, G, K)));
	}

	private Corner<GraphMorphism> determinePushOutComplement(Corner<TotalFunction> pc_V, Corner<TotalFunction> pc_E, Graph G, Graph K) {
		FinSet E_D = pc_E.first.trg();
		FinSet V_D = pc_V.first.trg();
		
		// TODO (3) Construct the pushout complement as Graph D, k: K -> D, and l': D -> G
		throw new UnsupportedOperationException("Not implemented yet");
	}

	private Collection<Object> determineDanglingEdges(Corner<GraphMorphism> upperLeft) {
		Graph L = upperLeft.first.trg();
		Graph G = upperLeft.second.trg();
		GraphMorphism m = upperLeft.second;
		GraphMorphism l = upperLeft.first;
		
		// TODO (4) Implement the following steps:
		
		// Determine dangling points
		
		// Determine gluing points
		
		// Check the dangling edge condition to determine all dangling edges
	
		throw new UnsupportedOperationException("Not implemented yet");
	}

	private boolean edgeIsIncidentToVertice(Graph G, GraphMorphism m, Object v, Object e) {
		return G.src().map(e).equals(m._V().map(v)) || G.trg().map(e).equals(m._V().map(v));
	}

	public Corner<GraphMorphism> restrict(Corner<GraphMorphism> upperLeft) {
		Graph G = upperLeft.second.trg();
		Graph L = upperLeft.first.trg();
		GraphMorphism m = upperLeft.second;
		
		Collection<Object> danglingEdges = determineDanglingEdges(upperLeft);
		GraphMorphism _g_ =  G.removeEdges(danglingEdges);
		
		// TODO (6) Construct a restriction of the match m: L -> G to be used for rule application
	
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
