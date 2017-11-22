package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import static org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphs.TripleGraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.slice.Slice;
import org.upb.fmde.de.categories.slice.Triangle;

public class TypedTripleGraphs extends    Slice<TripleGraph,TripleMorphism>
                               implements LabelledCategory<TripleMorphism, Triangle<TripleGraph, TripleMorphism>> {
    
	/**
	 * Create a typed triple graph category for the given type triple graph
	 * @param typeTripleGraph typing object
	 * @return typed triple graph category
	 */
	public static TypedTripleGraphs TypedTripleGraphsFor(TripleGraph typeTripleGraph) {
		return new TypedTripleGraphs(typeTripleGraph);
	}
	
	/**
	 * Create typed triple graphs as a slice category
	 * @param typeTripleGraph typing/slicing object
	 */
	public TypedTripleGraphs(TripleGraph typeTripleGraph) {
		super(TripleGraphs.TripleGraphs,typeTripleGraph);
	}

}
