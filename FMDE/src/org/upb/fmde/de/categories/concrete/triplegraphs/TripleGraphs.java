package org.upb.fmde.de.categories.concrete.triplegraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TripleGraphs implements LabelledCategory<TripleGraph, TripleMorphism>,
									CategoryWithInitOb<TripleGraph, TripleMorphism>{

	public static TripleGraphs TripleGraphs = new TripleGraphs();
	
	private final Graph EMPTY_GRAPH = Graphs.initialObject().obj;
	private final GraphMorphism emptyGraphArrow = Graphs.initialObject().up.apply(EMPTY_GRAPH);
	private final TripleGraph EMPTY_TRIPLE = new TripleGraph("EMPTY_TRIPLE", EMPTY_GRAPH, emptyGraphArrow, EMPTY_GRAPH, emptyGraphArrow, EMPTY_GRAPH);
	private final CoLimit<TripleGraph, TripleMorphism> INITIAL_OBJECT = 
			new CoLimit<>(
					EMPTY_TRIPLE,
					GT ->  new TripleMorphism("initial_" + GT.label(), EMPTY_TRIPLE, GT, 
					Graphs.initialObject().up.apply(GT.getG_S()), 
					Graphs.initialObject().up.apply(GT.getG_C()),
					Graphs.initialObject().up.apply(GT.getG_T()))
			);
	
	@Override
	public TripleMorphism compose(TripleMorphism f, TripleMorphism g) {
		return new TripleMorphism(f.label() + ";" + g.label(), f.src(), g.trg(), 
				Graphs.compose(f.getF_S(), g.getF_S()), 
				Graphs.compose(f.getF_C(), g.getF_C()), 
				Graphs.compose(f.getF_T(), g.getF_T()));
	}

	@Override
	public TripleMorphism id(TripleGraph f) {
		return new TripleMorphism("id_" + f.label(), f, f,
				Graphs.id(f.getG_S()), 
				Graphs.id(f.getG_C()), 
				Graphs.id(f.getG_T()));
	}

	@Override
	public CoLimit<TripleGraph, TripleMorphism> initialObject() {
		return INITIAL_OBJECT;
	}
}
