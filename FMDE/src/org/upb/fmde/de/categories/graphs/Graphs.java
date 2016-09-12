package org.upb.fmde.de.categories.graphs;

import static org.upb.fmde.de.categories.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.LabelledCategory;

public class Graphs implements LabelledCategory<Graph, GraphMorphism>, CategoryWithInitOb<Graph, GraphMorphism> {

	public static Graphs Graphs = new Graphs();
	private static final Graph EMPTY_GRAPH = new Graph("INITIAL_OB", FinSets.initialObject(), FinSets.initialObject(), FinSets.initialArrowInto(FinSets.initialObject()), FinSets.initialArrowInto(FinSets.initialObject()));
	
	@Override
	public GraphMorphism compose(GraphMorphism f, GraphMorphism g) {
		return new GraphMorphism(
			f.label() + ";" + g.label(),
			f.getSource(),
			g.getTarget(),
			FinSets.compose(f.get_f_E(), g.get_f_E()),
			FinSets.compose(f.get_f_V(), g.get_f_V())
		);
	}

	@Override
	public GraphMorphism id(Graph g) {
		return new GraphMorphism("id_" + g.label(), g, g, FinSets.id(g.getEdges()), FinSets.id(g.getVertices()));
	}

	@Override
	public Graph initialObject() {
		return EMPTY_GRAPH;
	}

	@Override
	public GraphMorphism initialArrowInto(Graph g) {
		return new GraphMorphism("initial_" + g.label(), EMPTY_GRAPH, g, FinSets.initialArrowInto(g.getEdges()), FinSets.initialArrowInto(g.getVertices()));
	}
}
