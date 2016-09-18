package org.upb.fmde.de.categories.concrete.triplegraphs;

import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TripleGraph extends Labelled {

	private Graph G_S;
	private Graph G_C;
	private Graph G_T;
	
	private GraphMorphism sigma;
	private GraphMorphism tau;
	
	
	public TripleGraph(String label, Graph G_S, GraphMorphism sigma, Graph G_C, GraphMorphism tau, Graph G_T) {
		super(label);
		this.G_S = G_S;
		this.G_C = G_C;
		this.G_T = G_T;
		this.sigma = sigma;
		this.tau = tau;
	}


	public Graph getG_S() {
		return G_S;
	}


	public Graph getG_C() {
		return G_C;
	}


	public Graph getG_T() {
		return G_T;
	}


	public GraphMorphism getSigma() {
		return sigma;
	}


	public GraphMorphism getTau() {
		return tau;
	}
}
