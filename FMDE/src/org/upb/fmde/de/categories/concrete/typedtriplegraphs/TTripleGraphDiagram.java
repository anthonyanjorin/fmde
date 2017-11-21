package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphDiagram;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.slice.Triangle;

public class TTripleGraphDiagram extends Diagram<TripleMorphism, Triangle<TripleGraph, TripleMorphism>> {

	public TTripleGraphDiagram(TripleGraph typeTripleGraph) {
		super(TypedTripleGraphs.TypedTripleGraphsFor(typeTripleGraph));
	}

	public TripleGraphDiagram getTripleGraphDiagram() {
		TripleGraphDiagram d =  new TripleGraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getF()));
		getObjects().forEach(o -> d.objects(o.src(), o.trg()));
		getObjects().forEach(o -> d.arrows(o));
		return d;
	}
}
