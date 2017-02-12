package org.upb.fmde.de.categories.concrete.slicecat;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;

public class SliceCategory<Ob, Arr extends LabelledArrow<Ob>> implements Category<Arr, Triangle<Ob, Arr>> {

private Category<Ob, Arr> cat;
private Ob X;
	
	public SliceCategory(Category<Ob, Arr> cat , Ob X ) {
		this.X = X;
		this.cat = cat;
		
	}
	
	
	@Override
	public Triangle<Ob, Arr> compose(Triangle<Ob, Arr> f, Triangle<Ob, Arr> g) {
		f.checkValidity(X);
		g.checkValidity(X);
		return new Triangle<Ob, Arr>(f.label()+";"+g.label(),cat.compose(f.getM(), g.getM()),f.getF(),g.getG());
	}

	@Override
	public Triangle<Ob, Arr> id(Arr o) {
		return new Triangle<Ob, Arr>("id_"+o.label(),cat.id(cat.source(o)),cat.id(cat.source(o)),cat.id(cat.source(o)));
		
	}

	@Override
	public Arr source(Triangle<Ob, Arr> f) {
		f.checkValidity(X);
		return f.getF();
	}

	@Override
	public Arr target(Triangle<Ob, Arr> f) {
		f.checkValidity(X);
		return f.getG();
	}

	@Override
	public String showOb(Arr o) {
		if (o.trg().equals(X))
			return cat.showArr(o);
		else throw new IllegalArgumentException(o.label()+" is no Object in sliced category!");
	}

	@Override
	public String showArr(Triangle<Ob, Arr> f) {
		f.checkValidity(X);
		return f.getF().label() + "-" + f.getM().label() + "-" + f.getG().label();
	}
	

}
