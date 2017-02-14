package org.upb.fmde.de.categories.concrete.pfinsets;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;

public class PFinSets implements LabelledCategory<FinSet, PartialFunction>,
		CategoryWithInitOb<FinSet, PartialFunction>, CategoryWithPushouts<FinSet, PartialFunction> {

	public static PFinSets PFinSets = new PFinSets();

	@Override
	public PartialFunction compose(PartialFunction f, PartialFunction g) {
		PartialFunction f_then_g = new PartialFunction(f.src(), f + ";" + g, g.trg());
		f.src().elts().forEach(x -> {
			Object mapping = g.map(f.map(x));
			if (mapping != null) {
				f_then_g.addMapping(x, mapping);
			}
		});
		return f_then_g;
	}
	
	@Override
	public PartialFunction id(FinSet a) {
		PartialFunction id = new PartialFunction(a, "id_" + a, a);
		a.elts().forEach(x -> id.addMapping(x, x));
		return id;
	}

	@Override
	public CoLimit<PartialFunction, PartialFunction> coequaliser(PartialFunction f, PartialFunction g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoLimit<CoSpan<PartialFunction>, PartialFunction> coproduct(FinSet a, FinSet b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoLimit<FinSet, PartialFunction> initialObject() {
		// TODO Auto-generated method stub
		return null;
	}


}
