package org.upb.fmde.de.categories.finsets;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.LabelledCategory;

public class FinSets implements LabelledCategory<FinSet, TotalFunction>, CategoryWithInitOb<FinSet, TotalFunction> {
	
	public static FinSets FinSets = new FinSets(); 
	private static final FinSet EMPTY_FINSET = new FinSet("INITIAL_OB");

	@Override
	public TotalFunction compose(TotalFunction f, TotalFunction g) {
		TotalFunction f_then_g = new TotalFunction(f.getSource(), f + ";" + g, g.getTarget());
		f.getSource().getElements().forEach(x -> f_then_g.addMapping(x, g.map(f.map(x))));
		return f_then_g;
	}

	@Override
	public TotalFunction id(FinSet a) {
		return new TotalFunction(a, "id_" + a, a);
	}

	@Override
	public TotalFunction initialArrowInto(FinSet o) {
		return new TotalFunction(EMPTY_FINSET, "initial_" + o.label(), o);
	}

	@Override
	public FinSet initialObject() {
		return EMPTY_FINSET;
	}
}
