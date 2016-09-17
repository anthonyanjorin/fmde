package org.upb.fmde.de.categories.triplegraphs;

import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.graphs.GraphDiagram;

public class TripleGraphDiagram extends Diagram<TripleGraph, TripleMorphism> {

	public TripleGraphDiagram() {
		super(TripleGraphs.TripleGraphs);
	}

	public GraphDiagram getGraphDiagram() {
		GraphDiagram d = new GraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getF_S(), a.getF_C(), a.getF_T()));
		getObjects().forEach(o -> d.objects(o.getG_S(), o.getG_C(), o.getG_T()));
		getObjects().forEach(o -> d.arrows(o.getSigma(), o.getTau()));
		return d;
	}
}
