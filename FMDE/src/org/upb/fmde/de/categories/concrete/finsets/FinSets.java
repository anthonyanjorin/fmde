package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;

public class FinSets implements LabelledCategory<FinSet, TotalFunction>, 
							    CategoryWithInitOb<FinSet, TotalFunction>{
	
	public static FinSets FinSets = new FinSets(); 
	
	@Override
	public TotalFunction compose(TotalFunction f, TotalFunction g) {
		TotalFunction f_then_g = new TotalFunction(f.src(), f + ";" + g, g.trg());
		f.src().elts().forEach(x -> f_then_g.addMapping(x, g.map(f.map(x))));
		return f_then_g;
	}

	@Override
	public TotalFunction id(FinSet a) {
		TotalFunction id =  new TotalFunction(a, "id_" + a, a);
		a.elts().forEach(x -> id.addMapping(x, x));
		return id;
	}

	@Override
	public CoLimit<FinSet, TotalFunction> initialObject() {
		// TODO (10) Implement Def 8 for FinSets
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
