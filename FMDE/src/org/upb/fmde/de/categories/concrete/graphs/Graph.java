package org.upb.fmde.de.categories.concrete.graphs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

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
		Category.ensure(source.src().equals(E), "Graph " + label + " is invalid: " + "source function doesn't fit to set of edges");
		Category.ensure(source.trg().equals(V), "Graph " + label + " is invalid: " + "source function doesn't fit to set of vertices");
		Category.ensure(target.src().equals(E), "Graph " + label + " is invalid: " + "target function doesn't fit to set of edges");
		Category.ensure(target.trg().equals(V), "Graph " + label + " is invalid: " + "target function doesn't fit to set of vertices");
	}
	
	public FinSet edges(){
		return E;
	}
	
	public FinSet vertices(){
		return V;
	}
	
	public TotalFunction src(){
		return source;
	}
	
	public TotalFunction trg(){
		return target;
	}
}
