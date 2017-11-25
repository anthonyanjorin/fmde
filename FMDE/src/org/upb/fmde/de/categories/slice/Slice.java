package org.upb.fmde.de.categories.slice;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;

public class Slice<Ob, Arr extends LabelledArrow<Ob>> implements CategoryWithInitOb<Arr, Triangle<Ob, Arr>>{
	
	protected Ob T;
	protected Category<Ob, Arr> cat;
	
	private final Arr UNDERLYING_INITIAL_OBJECT;
	private final CoLimit<Arr, Triangle<Ob, Arr>> INITIAL_OBJECT;
	
	/**
	 * Creates the slice category out of an underlying category and a slicing object T.
	 * Computes the initial object based on the underlying category.
	 * @param cat underlying category
	 * @param T slicing object
	 */
	public Slice(CategoryWithInitOb<Ob, Arr> cat, Ob T) {
		this.cat = cat;
		this.T = T;

		UNDERLYING_INITIAL_OBJECT = cat.initialObject().up.apply(T);
		INITIAL_OBJECT = new CoLimit<>(
			UNDERLYING_INITIAL_OBJECT, 
			O -> new Triangle<Ob, Arr>("initial_" + O.label(), cat.initialObject().up.apply(O.src()), UNDERLYING_INITIAL_OBJECT, O)
		);
	}
	
	/**
	 * Composes two triangles by taking the first arrow of the first triangle and the second arrow of the second triangle
	 * as new arrows to the slicing object
	 * @param f first triangle 
	 * @param g second triangle 
	 * @return composed triangle
	 */
	@Override
	public Triangle<Ob, Arr> compose(Triangle<Ob, Arr> f, Triangle<Ob, Arr> g) {
		f.checkValidity(T);
		g.checkValidity(T);
		//TODO Return the composition of f and g according to the lecture. The composition within the underlying 
		//     category can be reused. Take a look at the composition in the Category class and think about which 
		//     parameters to pass here.
		
		return null;
	}

	@Override
	public Triangle<Ob, Arr> id(Arr o) {
		//TODO Create the id arrow by reusing the id arrow of the underlying category. 
		return null;
	}

	@Override
	public Arr source(Triangle<Ob, Arr> f) {
		f.checkValidity(T);
		return f.getType();
	}

	@Override
	public Arr target(Triangle<Ob, Arr> f) {
		f.checkValidity(T);
		return f.getType_();
	}
	
	@Override
	public String showOb(Arr o) {
		if (o.trg().equals(T)) return cat.showArr(o);
		else throw new IllegalArgumentException(o.label()+" is not an object in the sliced category!");
	}

	@Override
	public String showArr(Triangle<Ob, Arr> f) {
		f.checkValidity(T);
		return f.getType().label() + "-" + f.getF().label() + "-" + f.getType_().label();
	}

	@Override
	public CoLimit<Arr, Triangle<Ob, Arr>> initialObject() {
		return INITIAL_OBJECT;
	}
}
