package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Span;

public class FinSets implements LabelledCategory<FinSet, TotalFunction>, 
							    CategoryWithInitOb<FinSet, TotalFunction>,
							    CategoryWithPushouts<FinSet, TotalFunction> {
	
	public static FinSets FinSets = new FinSets(); 
	private final FinSet EMPTY_FINSET = new FinSet("EMPTY_FINSET");
	private final CoLimit<FinSet, TotalFunction> INITIAL_OBJECT 
		= new CoLimit<>(
			EMPTY_FINSET, 
			o -> new TotalFunction(EMPTY_FINSET, "initial_" + o.label(), o
		));

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
		return INITIAL_OBJECT;
	}

	@Override
	public CoLimit<TotalFunction, TotalFunction> coequaliser(TotalFunction f, TotalFunction g) {
		// TODO
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public CoLimit<CoSpan<TotalFunction>, TotalFunction> coproduct(FinSet R, FinSet G) {
		// TODO
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
