package org.upb.fmde.de.categories.finsets;

import org.upb.fmde.de.categories.CategoryWithInitOb;
import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.pushouts.CoSpan;
import org.upb.fmde.de.categories.pushouts.Span;

public class FinSets implements LabelledCategory<FinSet, TotalFunction>, 
							    CategoryWithInitOb<FinSet, TotalFunction>,
							    CategoryWithPushouts<FinSet, TotalFunction>{
	
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
		TotalFunction id =  new TotalFunction(a, "id_" + a, a);
		a.getElements().forEach(x -> id.addMapping(x, x));
		return id;
	}

	@Override
	public TotalFunction initialArrowInto(FinSet o) {
		return new TotalFunction(EMPTY_FINSET, "initial_" + o.label(), o);
	}

	@Override
	public FinSet initialObject() {
		return EMPTY_FINSET;
	}

	@Override
	public TotalFunction coequaliser(CoSpan<TotalFunction> cospan) {
		// Check to see that cospan is actually a square
		new Span<>(FinSets, cospan.horizontal, cospan.vertical);
		
		FinSet result = new FinSet("==", cospan.horizontal.getTarget().getElements());
		TotalFunction coequaliser = new TotalFunction(cospan.horizontal.getTarget(), "==", result);
		coequaliser.setMappings(FinSets.id(result).getMappings());
		
		for(Object o : cospan.horizontal.getSource().getElements()){
			Object im_v = cospan.vertical.map(o);
			Object im_h = cospan.horizontal.map(o);
			
			if(!im_v.equals(im_h)){
				result.getElements().remove(im_h);
				coequaliser.addMapping(im_h, im_v);
			}
		}
		
		return coequaliser;
	}

	@Override
	public CoSpan<TotalFunction> coproduct(Span<TotalFunction> span) {
		FinSet R = span.horizontal.getTarget();
		FinSet G = span.vertical.getTarget();
		
		FinSet G_Plus_R = new FinSet(G.label() + " + " + R.label(), G.getElements());
		G_Plus_R.getElements().addAll(R.getElements()); 
		
		TotalFunction horizontal = new TotalFunction(G, "G->G+R", G_Plus_R);
		for(int i = 0; i <  G.getElements().size(); i++){
			horizontal.addMapping(G.getElements().get(i), G_Plus_R.getElements().get(i));
		}
		
		TotalFunction vertical = new TotalFunction(R, "R->G+R", G_Plus_R);
		for(int j = 0, i = G.getElements().size(); i <  G_Plus_R.getElements().size(); j++, i++){
			vertical.addMapping(R.getElements().get(j), G_Plus_R.getElements().get(i));
		}
		
		return new CoSpan<TotalFunction>(FinSets, horizontal, vertical);
	}

	@Override
	public TotalFunction universalProperty(CoSpan<TotalFunction> pushout, CoSpan<TotalFunction> cospan) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
