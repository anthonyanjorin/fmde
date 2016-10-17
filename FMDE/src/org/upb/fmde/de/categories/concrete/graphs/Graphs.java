package org.upb.fmde.de.categories.concrete.graphs;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

public class Graphs implements LabelledCategory<Graph, GraphMorphism>, 
							   CategoryWithInitOb<Graph, GraphMorphism>, 
							   CategoryWithPushouts<Graph, GraphMorphism> {

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
		// TODO
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public CoLimit<CoSpan<GraphMorphism>, GraphMorphism> coproduct(Graph a, Graph b) {
		// TODO
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
