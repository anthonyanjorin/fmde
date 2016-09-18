package org.upb.fmde.de.categories.concrete.tgraphs;

import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TGraph extends Labelled {
	
	private GraphMorphism type;
	
	public TGraph(String label, GraphMorphism type) {
		super(label);
		this.type = type;
	}
	
	public GraphMorphism type(){
		return type;
	}
}
