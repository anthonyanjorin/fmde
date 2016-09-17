package org.upb.fmde.de.categories.graphs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.TotalFunction;

public class Graph extends Labelled {
	private FinSet E;
	private FinSet V;
	private TotalFunction source;
	private TotalFunction target;

	public Graph(String label, FinSet edges, FinSet vertices, TotalFunction source, TotalFunction target) {
		super(label);
		E = edges;
		V = vertices;
		this.source = source;
		this.target = target;
		ensureValidity();
	}

	private void ensureValidity() {
		Category.ensure(source.getSource().equals(E), "Graph " + label + " is invalid: " + "source function doesn't fit to set of edges");
		Category.ensure(source.getTarget().equals(V), "Graph " + label + " is invalid: " + "source function doesn't fit to set of vertices");
		Category.ensure(target.getSource().equals(E), "Graph " + label + " is invalid: " + "target function doesn't fit to set of edges");
		Category.ensure(target.getTarget().equals(V), "Graph " + label + " is invalid: " + "target function doesn't fit to set of vertices");
	}
	
	public FinSet getEdges(){
		return E;
	}
	
	public FinSet getVertices(){
		return V;
	}
	
	public TotalFunction src(){
		return source;
	}
	
	public TotalFunction trg(){
		return target;
	}
}
