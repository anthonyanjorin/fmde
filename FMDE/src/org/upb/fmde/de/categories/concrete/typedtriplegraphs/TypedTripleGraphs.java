package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import static org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphs.TripleGraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.slice.Slice;
import org.upb.fmde.de.categories.slice.Triangle;

public class TypedTripleGraphs extends    Slice<TripleGraph,TripleMorphism>
                               implements LabelledCategory<TripleMorphism, Triangle<TripleGraph, TripleMorphism>> {

	//public static final TypedTripleGraphs TypedTripleGraphs = new TypedTripleGraphs(null);
    
	public static TypedTripleGraphs TypedTripleGraphsFor(TripleGraph typeTripleGraph) {
		return new TypedTripleGraphs(typeTripleGraph);
	}
	
	public TypedTripleGraphs(TripleGraph typeTripleGraph) {
		super(TripleGraphs.TripleGraphs,typeTripleGraph);
	}

}
