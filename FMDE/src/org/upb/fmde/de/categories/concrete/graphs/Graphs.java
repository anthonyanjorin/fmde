package org.upb.fmde.de.categories.concrete.graphs;

import org.upb.fmde.de.categories.LabelledCategory;

public class Graphs implements LabelledCategory<Graph, GraphMorphism> {

	public static Graphs Graphs = new Graphs();
	
	@Override
	public GraphMorphism compose(GraphMorphism f, GraphMorphism g) {
		// TODO (03) Implement graph morphism composition
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	@Override
	public GraphMorphism id(Graph g) {
		// TODO (04) Id graph morphism 
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}
}
