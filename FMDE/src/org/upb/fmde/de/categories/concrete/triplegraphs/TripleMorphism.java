package org.upb.fmde.de.categories.concrete.triplegraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class TripleMorphism extends LabelledArrow<TripleGraph> implements ComparableArrow<TripleMorphism> {

	private GraphMorphism f_S;
	private GraphMorphism f_C;
	private GraphMorphism f_T;
	
	public TripleMorphism(String label, TripleGraph source, TripleGraph target, GraphMorphism f_S, GraphMorphism f_C, GraphMorphism f_T) {
		super(label, source, target);
		this.f_S = f_S;
		this.f_C = f_C;
		this.f_T = f_T;
		if(!isValid()) throw new IllegalArgumentException("TripleMorphism " + label + " = (" + f_S.label() + ", " + f_C.label() + ", " + f_T.label() + ") is not valid.");
	}

	private boolean isValid() {
		return Graphs.compose(f_C, target.getSigma()).isTheSameAs(Graphs.compose(source.getSigma(), f_S)) &&
			   Graphs.compose(f_C, target.getTau()).isTheSameAs(Graphs.compose(source.getTau(), f_T));
	}

	@Override
	public boolean isTheSameAs(TripleMorphism a) {
		return f_S.isTheSameAs(a.f_S) &&
			   f_C.isTheSameAs(a.f_C) &&
			   f_T.isTheSameAs(a.f_T);
	}

	public GraphMorphism getF_S() {
		return f_S;
	}
	
	public GraphMorphism getF_C() {
		return f_C;
	}
	
	public GraphMorphism getF_T() {
		return f_T;
	}
}
