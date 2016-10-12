package org.upb.fmde.de.categories.concrete.graphs;

import java.util.Collection;

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

	public GraphMorphism removeEdges(Collection<Object> edgesToBeRemoved) {
		FinSet E_ = new FinSet("E_", E.elts());
		E_.elts().removeAll(edgesToBeRemoved);
		
		TotalFunction source_ = new TotalFunction(E_, "source_", V);
		E_.elts().forEach(e -> source_.addMapping(e, source.map(e)));
		
		TotalFunction target_ = new TotalFunction(E_, "target_", V);
		E_.elts().forEach(e -> target_.addMapping(e, target.map(e)));
		
		Graph G_ = new Graph("G_", E_, V, source_, target_);
		
		GraphMorphism id_G_ = Graphs.Graphs.id(G_);
		
		TotalFunction G_E = new TotalFunction(G_.E, "G_E", this.E);
		G_.E.elts().forEach(e -> G_E.addMapping(e, id_G_._E().map(e)));
		
		TotalFunction G_V = new TotalFunction(G_.V, "G_V", this.V);
		G_.V.elts().forEach(v -> G_V.addMapping(v, id_G_._V().map(v)));
		
		return new GraphMorphism("_g_", G_, this, G_E, G_V);
	}
}
