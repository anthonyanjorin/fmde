package org.upb.fmde.de.categories.slice;

import java.util.Optional;

import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushoutComplements;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;

public class SliceWithPushoutComplements<Ob, Arr extends LabelledArrow<Ob>> extends Slice<Ob, Arr> 
																		  implements CategoryWithPushoutComplements<Arr, Triangle<Ob, Arr>>
{
	private CategoryWithPushoutComplements<Ob, Arr> cat;
	
	/**
	 * Make sure that the given category supports pushout complements.
	 * 
	 * @param cat
	 * @param T
	 */  
	@SuppressWarnings("unchecked")
	public SliceWithPushoutComplements(CategoryWithInitOb<Ob, Arr> cat, Ob T) {
		super(cat, T);
		this.cat = (CategoryWithPushoutComplements<Ob, Arr>) cat;  } 

	@Override
	public CoLimit<Triangle<Ob, Arr>, Triangle<Ob, Arr>> coequaliser(Triangle<Ob, Arr> f, Triangle<Ob, Arr> g) {
		CoLimit<Arr, Arr> coeq_graphs = cat.coequaliser(f.getF(), g.getF());
		Arr type = coeq_graphs.up.apply(f.trg());
		Triangle<Ob, Arr> coeq_obj = new Triangle<Ob, Arr>("==", coeq_graphs.obj, f.trg(), type);
		
		return new CoLimit<Triangle<Ob, Arr>, Triangle<Ob, Arr>>(
				coeq_obj, 
				tm -> new Triangle<Ob, Arr>("u", coeq_graphs.up.apply(tm.getF()), type, tm.trg())
				);
	}

	@Override
	public CoLimit<CoSpan<Triangle<Ob, Arr>>, Triangle<Ob, Arr>> coproduct(Arr a, Arr b) {
		CoLimit<CoSpan<Arr>, Arr> coprod_graphs = cat.coproduct(a.src(), b.src());
		Arr type = coprod_graphs.up.apply(new CoSpan<>(cat, b, a));
		
		return new CoLimit<>(
				new CoSpan<Triangle<Ob, Arr>>(this, new Triangle<Ob, Arr>("left", coprod_graphs.obj.left, b, type), 
						     		new Triangle<Ob, Arr>("right", coprod_graphs.obj.right, a, type)), 
				cos -> new Triangle<Ob, Arr>("u", 
						coprod_graphs.up.apply(new CoSpan<Arr>(cat, cos.left.getF(), cos.right.getF())),
						type,
						cos.left.trg())
				);
	}

	@Override
	public Optional<Corner<Triangle<Ob, Arr>>> pushoutComplement(Corner<Triangle<Ob, Arr>> upperLeft) {
		Triangle<Ob, Arr> l = upperLeft.first;
		Triangle<Ob, Arr> m = upperLeft.second;
		Arr K = l.src();
		Arr G = m.trg();
		
		return cat.pushoutComplement(new Corner<Arr>(cat, l.getF(), m.getF()))
				.map(pc_G -> determineTypedPushoutComplement(pc_G, K, G));
	}
	
	private Corner<Triangle<Ob, Arr>> determineTypedPushoutComplement(Corner<Arr> pc_G, Arr K, Arr G) {
		Arr type_D = cat.compose(pc_G.second, G);
		
		Triangle<Ob, Arr> d = new Triangle<Ob, Arr>("d", pc_G.first, K, type_D);
		Triangle<Ob, Arr> l_ = new Triangle<Ob, Arr>("l'", pc_G.second, type_D, G);
		
		return new Corner<>(this, d, l_);
	}

	@Override
	public Corner<Triangle<Ob, Arr>> restrict(Corner<Triangle<Ob, Arr>> upperLeft) {
		Corner<Arr> untyped = cat.restrict(new Corner<Arr>(cat, upperLeft.first.getF(), upperLeft.second.getF()));		
		Arr type_G = cat.compose(untyped.second, upperLeft.second.trg());
		
		return new Corner<Triangle<Ob, Arr>>(this, new Triangle<Ob, Arr>("_m_", untyped.first, upperLeft.first.trg(), type_G),
								  				  new Triangle<Ob, Arr>("_g_", untyped.second, type_G, upperLeft.second.trg()));
	}
}
