package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphDiagram;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class TTripleGraphDiagram extends Diagram<TTripleGraph, TTripleMorphism> {

	public TTripleGraphDiagram() {
		super(TypedTripleGraphs.TypedTripleGraphs);
	}

	public TripleGraphDiagram getTripleGraphDiagram() {
		TripleGraphDiagram d =  new TripleGraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getUntypedMorphism()));
		getObjects().forEach(o -> d.objects(o.getTypeMorphism().src(), o.getTypeMorphism().trg()));
		getObjects().forEach(o -> d.arrows(o.getTypeMorphism()));
		return d;
	}
}
