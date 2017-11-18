package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;
import org.upb.fmde.de.categories.concrete.graphs.*;


import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.slice.Triangle;

public class TGraphMorphism extends Triangle<Graph,GraphMorphism> {

	private GraphMorphism f;
	
	public TGraphMorphism(String label, GraphMorphism f, TGraph source, TGraph target) {
		super(label, source, f, target);
		this.f = f;
		if(!isValid()) throw new IllegalArgumentException("Typed GraphMorphism " + label + ": " + f.src().label() + " -> " + f.trg().label() + " is not valid.");
	}

	private boolean isValid() {
		return Graphs.compose(f, target)
				     .isTheSameAs(source);
	}

	public GraphMorphism untyped(){
		return f;
	}
	
	@Override
	public void label(String label) {
		super.label(label);
		f.label(label);
	}
	
	/*@Override
	public boolean isTheSameAs(TGraphMorphism a) {
		return source.isTheSameAs(a.source) &&
			   target.isTheSameAs(a.trg()) &&
			   f.isTheSameAs(a.f);
	}*/
}
