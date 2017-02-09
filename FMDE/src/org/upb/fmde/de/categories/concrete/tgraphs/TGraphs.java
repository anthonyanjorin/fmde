package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.slicecat.SliceCategory;
import org.upb.fmde.de.categories.concrete.slicecat.Triangle;

public class TGraphs extends SliceCategory<Graph,GraphMorphism>  {

	
	public static Category<GraphMorphism, Triangle<Graph,GraphMorphism>> TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		super(Graphs.Graphs,typeGraph);
	}

}
