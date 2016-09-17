package org.upb.fmde.de.categories.typedtriplegraphs;

import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.triplegraphs.TripleGraphDiagram;

public class TTripleGraphDiagram extends Diagram<TTripleGraph, TTripleMorphism> {

	public TTripleGraphDiagram() {
		super(TypedTripleGraphs.TypedTripleGraphs);
	}

	public TripleGraphDiagram getTripleGraphDiagram() {
		TripleGraphDiagram d =  new TripleGraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getUntypedMorphism()));
		getObjects().forEach(o -> d.objects(o.getTypeMorphism().getSource(), o.getTypeMorphism().getTarget()));
		getObjects().forEach(o -> d.arrows(o.getTypeMorphism()));
		return d;
	}
}
