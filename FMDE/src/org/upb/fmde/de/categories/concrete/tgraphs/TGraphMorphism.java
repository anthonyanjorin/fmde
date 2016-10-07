package org.upb.fmde.de.categories.concrete.tgraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TGraphMorphism extends LabelledArrow<TGraph> implements ComparableArrow<TGraphMorphism> {

	private GraphMorphism f;
	
	public TGraphMorphism(String label, GraphMorphism f, TGraph source, TGraph target) {
		super(label, source, target);
		this.f = f;
		if(!isValid()) throw new IllegalArgumentException("Typed GraphMorphism " + label + ": " + f.src().label() + " -> " + f.trg().label() + " is not valid.");
	}

	private boolean isValid() {
		// TODO (05) Implement check for structure preservation
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}

	public GraphMorphism untyped(){
		return f;
	}
	
	@Override
	public void label(String label) {
		super.label(label);
		f.label(label);
	}
	
	@Override
	public boolean isTheSameAs(TGraphMorphism a) {
		// TODO (06) Equality check for typed graph morphisms
		throw new UnsupportedOperationException("Has not yet been implemented.");
	}
}
