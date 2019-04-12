package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledCategory;

public class FinSets implements LabelledCategory<FinSet, TotalFunction> {

	public static FinSets FinSets = new FinSets();

	@Override
	public TotalFunction compose(TotalFunction f, TotalFunction g) {
		Category.ensure(f.trg().equals(g.src()), "Cannot compose " + f.label() + " and " + g.label());

		var f_then_g = new TotalFunction(f.src(), f + ";" + g, g.trg());
		f.src().elts().forEach(x -> f_then_g.addMapping(x, g.map(f.map(x))));
		return f_then_g;
	}

	@Override
	public TotalFunction id(FinSet a) {
		var id = new TotalFunction(a, "id_" + a, a);
		a.elts().forEach(x -> id.addMapping(x, x));
		return id;
	}
}
