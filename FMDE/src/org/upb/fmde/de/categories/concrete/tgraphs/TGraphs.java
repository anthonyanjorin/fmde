package org.upb.fmde.de.categories.concrete.tgraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.slice.SliceWithPushoutComplements;
import org.upb.fmde.de.categories.slice.Triangle;

public class TGraphs extends    SliceWithPushoutComplements<Graph,GraphMorphism>
                     implements LabelledCategory<GraphMorphism, Triangle<Graph, GraphMorphism>> {
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	/**
	 * Create typed graphs as slice category
	 * @param typeGraph distinguished graph as typing/slicing object 
	 */
	public TGraphs(Graph typeGraph) {
		super(Graphs.Graphs, typeGraph);
	}
}
