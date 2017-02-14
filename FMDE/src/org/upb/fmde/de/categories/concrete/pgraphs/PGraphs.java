package org.upb.fmde.de.categories.concrete.pgraphs;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;
import static org.upb.fmde.de.categories.concrete.pfinsets.PFinSets.PFinSets;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;

public class PGraphs implements LabelledCategory<Graph, PGraphMorphism> {

	public static PGraphs PGraphs = new PGraphs();

	private final FinSet emptySet = FinSets.initialObject().obj;
	private final TotalFunction emptySetArrow = FinSets.initialObject().up.apply(emptySet);
	private final Graph EMPTY_GRAPH = new Graph("EMPTY_GRAPH", emptySet, emptySet, emptySetArrow, emptySetArrow);


	@Override
	public PGraphMorphism compose(PGraphMorphism f, PGraphMorphism g) {
		return new PGraphMorphism(f.label() + ";" + g.label(), f.src(), g.trg(), PFinSets.compose(f._E(), g._E()),
				PFinSets.compose(f._V(), g._V()));
	}

	@Override
	public PGraphMorphism id(Graph g) {
		return new PGraphMorphism("id_" + g.label(), g, g, PFinSets.id(g.edges()), PFinSets.id(g.vertices()));
	}


	
}
