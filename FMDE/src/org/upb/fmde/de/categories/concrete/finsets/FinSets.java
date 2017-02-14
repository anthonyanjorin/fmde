package org.upb.fmde.de.categories.concrete.finsets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushoutComplements;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.pfinsets.PartialFunction;

public class FinSets implements LabelledCategory<FinSet, TotalFunction>, 
							    CategoryWithInitOb<FinSet, TotalFunction>,
							    CategoryWithPushouts<FinSet, TotalFunction>,
							    CategoryWithPushoutComplements<FinSet, TotalFunction>{
	
	public static FinSets FinSets = new FinSets(); 
	private final FinSet EMPTY_FINSET = new FinSet("EMPTY_FINSET");
	private final CoLimit<FinSet, TotalFunction> INITIAL_OBJECT 
		= new CoLimit<>(
			EMPTY_FINSET, 
			o -> new TotalFunction(EMPTY_FINSET, "initial_" + o.label(), o
		));

	@Override
	public TotalFunction compose(TotalFunction f, TotalFunction g) {
		return this.compose((PartialFunction)f, (PartialFunction)g);
	}
	
	public TotalFunction compose(PartialFunction f, PartialFunction g) {
		TotalFunction f_then_g = new TotalFunction(f.src(), f + ";" + g, g.trg());
		f.src().elts().forEach(x -> {
			Object mapping = g.map(f.map(x));
			if (mapping == null) {
				throw new IllegalArgumentException("Functions cannot be composed.");
			}
			f_then_g.addMapping(x, mapping);
		});
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
		// Check to see that f and g are connected as expected
		new Span<>(FinSets, f, g);
		new CoSpan<>(FinSets, f, g);
		
		FinSet result = new FinSet("==", f.trg().elts());
		TotalFunction coequaliser = new TotalFunction(f.trg(), "==", result);
		coequaliser.setMappings(FinSets.id(result).mappings());
		
		for(Object o : f.src().elts()){
			Object im_v = g.map(o);
			Object im_h = f.map(o);
			
			if(!im_v.equals(im_h)){
				result.elts().remove(im_h);
				coequaliser.addMapping(im_h, im_v);
			}
		}
		
		return new CoLimit<TotalFunction, TotalFunction>(coequaliser,
				a -> {
					TotalFunction u = new TotalFunction(result, "u", a.trg());
					f.trg().elts().forEach(x -> u.addMapping(coequaliser.map(x), a.map(x)));
					return u;
				});
	}

	@Override
	public CoLimit<CoSpan<TotalFunction>, TotalFunction> coproduct(FinSet R, FinSet G) {
		FinSet G_Plus_R = new FinSet(G.label() + " + " + R.label(), G.elts());
		G_Plus_R.elts().addAll(R.elts()); 
		
		TotalFunction left = new TotalFunction(G, "G->G+R", G_Plus_R);
		for(int i = 0; i <  G.elts().size(); i++){
			left.addMapping(G.elts().get(i), G_Plus_R.elts().get(i));
		}
		
		TotalFunction right = new TotalFunction(R, "R->G+R", G_Plus_R);
		for(int j = 0, i = G.elts().size(); i <  G_Plus_R.elts().size(); j++, i++){
			right.addMapping(R.elts().get(j), G_Plus_R.elts().get(i));
		}
		
		return new CoLimit<CoSpan<TotalFunction>, TotalFunction>(
				new CoSpan<TotalFunction>(FinSets, left, right),
				cos -> {
					TotalFunction u = new TotalFunction(G_Plus_R, "u", cos.left.trg());
					R.elts().forEach(x -> u.addMapping(right.map(x), cos.right.map(x)));
					G.elts().forEach(x -> u.addMapping(left.map(x), cos.left.map(x)));
					return u;
				});
	}

	@Override
	public Optional<Corner<TotalFunction>> pushoutComplement(Corner<TotalFunction> upperLeft) {
		TotalFunction l = upperLeft.first;
		TotalFunction m = upperLeft.second;
		TotalFunction l_m = compose(l,m);
		FinSet K = l.src();
		FinSet L = l.trg();
		FinSet G = m.trg();
		
		Collection<Object> elts_D = new ArrayList<>(); 
		elts_D.addAll(G.elts());
		elts_D.removeAll(m.mappings().values());
		elts_D.addAll(l_m.mappings().values());
		FinSet D = new FinSet("D", elts_D.toArray());
		
		TotalFunction l_ = new TotalFunction(D, "l'", G);
		l_.mappings().putAll(id(D).mappings());
		
		TotalFunction d = new TotalFunction(K, "d", D);
		d.mappings().putAll(l_m.mappings());
		
		return Optional.of(new Corner<>(this, d, l_));
	}
}
