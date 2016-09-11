package org.upb.fmde.de.categories.graphs;

import static org.upb.fmde.de.categories.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.finsets.TotalFunction;

public class GraphMorphism extends LabelledArrow<Graph> implements ComparableArrow<GraphMorphism> {
	
	private TotalFunction f_E;
	private TotalFunction f_V;

	public GraphMorphism(String label, Graph srcGraph, Graph trgGraph, TotalFunction f_E, TotalFunction f_V) {
		super(label, srcGraph, trgGraph);
		this.f_E = f_E;
		this.f_V = f_V;
		ensureValidity();
	}

	private void ensureValidity(){
		String message = "GraphMorphism " + label + ": " + source.label() + " -> " + target.label() + " is not valid: ";
		ensure(f_E.getSource().equals(source.getEdges()), message + f_E.label() + " does not match the source graph " + source.label());
		ensure(f_E.getTarget().equals(target.getEdges()) , message + f_E.label() + " does not match the target graph " + target.label());
		ensure(f_V.getSource().equals(source.getVertices()) , message + f_V.label() + " does not match the source graph " + source.label());
		ensure(f_V.getTarget().equals(target.getVertices()) , message + f_V.label() + " does not match the target graph " + target.label());
		ensure(isStructurePreserving() , message + "It is not structure preserving!");		
	}

	private boolean isStructurePreserving() {
		return FinSets.compose(f_E, target.src()).
				isTheSameAs(FinSets.compose(source.src(), f_V)) &&
			   FinSets.compose(f_E, target.trg()).
				isTheSameAs(FinSets.compose(source.trg(), f_V));
	}

	public TotalFunction get_f_E(){
		return f_E;
	}
	
	public TotalFunction get_f_V(){
		return f_V;
	}

	@Override
	public boolean isTheSameAs(GraphMorphism a) {
		return a.get_f_E().isTheSameAs(f_E) &&
			   a.get_f_V().isTheSameAs(f_V);	
	}
}
