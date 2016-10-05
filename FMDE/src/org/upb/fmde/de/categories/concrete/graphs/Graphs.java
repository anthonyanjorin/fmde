package org.upb.fmde.de.categories.concrete.graphs;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

import java.util.Optional;
import java.util.stream.Collectors;

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
		
		TotalFunction horiz_s = FinSets.compose(b.src(), coprodVertices.obj.horiz);
		TotalFunction vert_s = FinSets.compose(a.src(), coprodVertices.obj.vert);
		TotalFunction u_s = coprodEdges.up.apply(new CoSpan<TotalFunction>(FinSets, horiz_s, vert_s));
		
		TotalFunction horiz_t = FinSets.compose(b.trg(), coprodVertices.obj.horiz);
		TotalFunction vert_t = FinSets.compose(a.trg(), coprodVertices.obj.vert);
		TotalFunction u_t = coprodEdges.up.apply(new CoSpan<TotalFunction>(FinSets, horiz_t, vert_t));
		
		Graph obj = new Graph(a.label() + " + " + b.label(), coprodEdges.obj.horiz.trg(), coprodVertices.obj.horiz.trg(), u_s, u_t);
		GraphMorphism horiz = new GraphMorphism("horiz", b, obj, coprodEdges.obj.horiz, coprodVertices.obj.horiz);
		GraphMorphism vert = new GraphMorphism("vert", a, obj, coprodEdges.obj.vert, coprodVertices.obj.vert);
		return new CoLimit<>(
				new CoSpan<>(this, horiz, vert), 
				cos -> {
					TotalFunction u_E = coprodEdges.up.apply(new CoSpan<>(FinSets, cos.horiz._E(), cos.vert._E()));
					TotalFunction u_V = coprodVertices.up.apply(new CoSpan<>(FinSets, cos.horiz._V(), cos.vert._V()));
					return new GraphMorphism("u", obj, cos.horiz.trg(), u_E, u_V);
				});
	}

	@Override
	public Optional<Corner<GraphMorphism>> pushoutComplement(Corner<GraphMorphism> upperLeft) {
		if(danglingEdgeConditionViolated(upperLeft))
			return Optional.empty();
		
		GraphMorphism l = upperLeft.first;
		GraphMorphism m = upperLeft.second;
		GraphMorphism l_m = compose(l, m);
		Graph K = l.src();
		Graph L = l.trg();
		Graph G = m.trg();
		
		FinSet V_D = new FinSet("V_D", K.vertices().elts()
				.stream()
				.map(v -> l_m._V().map(v))
				.collect(Collectors.toList()));
		
		FinSet E_D = new FinSet("E_D", K.edges().elts()
				.stream()
				.map(v -> l_m._E().map(v))
				.collect(Collectors.toList()));
		
		TotalFunction s_D = new TotalFunction(E_D, "s_D", V_D);
		G.src().mappings().forEach((from, to) -> {
			if(E_D.elts().contains(from)) 
				s_D.addMapping(from, to);
		});
		
		TotalFunction t_D = new TotalFunction(E_D, "t_D", V_D);
		G.trg().mappings().forEach((from, to) -> {
			if(E_D.elts().contains(from)) 
				t_D.addMapping(from, to);
		});
		
		Graph D = new Graph("D", E_D, V_D, s_D, t_D);
		
		TotalFunction l__E = new TotalFunction(D.edges(), "l'_E", G.edges());
		l__E.mappings().putAll(id(D)._E().mappings());
		
		TotalFunction l__V = new TotalFunction(D.vertices(), "l'_V", G.vertices());
		l__V.mappings().putAll(id(D)._V().mappings());
		
		GraphMorphism l_ = new GraphMorphism("l'", D, G, l__E, l__V);
		
		TotalFunction d_E = new TotalFunction(K.edges(), "d_E", D.edges());
		l__E.mappings().putAll(l_m._E().mappings());
		
		TotalFunction d_V = new TotalFunction(K.vertices(), "d_V", D.vertices());
		l__E.mappings().putAll(l_m._V().mappings());
		
		GraphMorphism d = new GraphMorphism("d", K, D, d_E, d_V);
		
		return Optional.of(new Corner<>(this, d, l_));
	}

	private boolean danglingEdgeConditionViolated(Corner<GraphMorphism> upperLeft) {
		// TODO 
		return false;
	}
}
