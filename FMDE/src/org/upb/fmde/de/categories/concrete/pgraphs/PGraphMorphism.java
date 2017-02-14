package org.upb.fmde.de.categories.concrete.pgraphs;

import static org.upb.fmde.de.categories.concrete.pfinsets.PFinSets.PFinSets;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.pfinsets.PartialFunction;

public class PGraphMorphism extends LabelledArrow<Graph> implements ComparableArrow<PGraphMorphism> {

	protected PartialFunction f_E;
	protected PartialFunction f_V;

	public PGraphMorphism(String label, Graph srcGraph, Graph trgGraph, PartialFunction f_E, PartialFunction f_V) {
		super(label, srcGraph, trgGraph);
		this.f_E = f_E;
		this.f_V = f_V;
		ensureValidity();
	}

	private void ensureValidity() {
		String message = "PGraphMorphism " + label + ": " + source.label() + " -> " + target.label()
				+ " is not valid: ";
		Category.ensure(f_E.src().equals(source.edges()),
				message + f_E.src().label() + " does not match " + source.edges().label());
		Category.ensure(f_E.trg().equals(target.edges()),
				message + f_E.trg().label() + " does not match " + target.edges().label());
		Category.ensure(f_V.src().equals(source.vertices()),
				message + f_V.src().label() + " does not match " + source.vertices().label());
		Category.ensure(f_V.trg().equals(target.vertices()),
				message + f_V.trg().label() + " does not match " + target.vertices().label());
		Category.ensure(isStructurePreserving(), message + "It is not structure preserving!");
	}

	private boolean isStructurePreserving() {
		PartialFunction fE_trgSource = PFinSets.compose(f_E, target.src());
		PartialFunction srcSource_fV = PFinSets.compose(source.src(), f_V);
		PartialFunction fE_trgTarget = PFinSets.compose(f_E, target.trg());
		PartialFunction srcTarget_fV = PFinSets.compose(source.trg(), f_V);
		
		boolean match_src = fE_trgSource.src().elts().stream()
		 .allMatch(x -> {
			 if (fE_trgSource.map(x) == null) {
				 return true;
			 }
			 return fE_trgSource.map(x).equals(srcSource_fV.map(x));
		 });
		
		boolean match_trg = fE_trgTarget.src().elts().stream()
				 .allMatch(x -> {
					 if (fE_trgTarget.map(x) == null) {
						 return true;
					 }
					 return fE_trgTarget.map(x).equals(srcTarget_fV.map(x));
				 });
		
		return match_src && match_trg;
		// return PFinSets.compose(f_E, target.src()).isTheSameAs(PFinSets.compose(source.src(), f_V)) && PFinSets.compose(f_E, target.trg()).isTheSameAs(PFinSets.compose(source.trg(), f_V));
	}

	public PartialFunction _E() {
		return f_E;
	}

	public PartialFunction _V() {
		return f_V;
	}

	@Override
	public boolean isTheSameAs(PGraphMorphism a) {
		return a._E().isTheSameAs(f_E) && a._V().isTheSameAs(f_V);
	}
}
