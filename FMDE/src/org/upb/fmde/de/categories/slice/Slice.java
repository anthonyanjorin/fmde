package org.upb.fmde.de.categories.slice;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;

public class Slice<Ob, Arr extends LabelledArrow<Ob>> implements Category<Arr, Triangle<Ob, Arr>>{
	
	private Ob X;
	private Category<Ob, Arr> cat;
	
	public Slice(Category<Ob, Arr> cat, Ob X) {
		this.cat = cat;
		this.X = X;
	}

	@Override
		public Triangle<Ob, Arr> compose(Triangle<Ob, Arr> f, Triangle<Ob, Arr> g) {
			((Triangle<Ob,Arr>)f).checkValidity(X);
			((Triangle<Ob,Arr>)g).checkValidity(X);
			return new Triangle<Ob, Arr>(f.label()+";"+g.label(),cat_L.compose(f.getM(), g.getM()),f.getF(),g.getG());
		}
	
		@Override
		public Triangle<Ob, Arr> id(Arr o) {
			return new Triangle<Ob, Arr>("id_"+o.label(),cat_L.id(cat_L.source(o)),cat_L.id(cat_L.source(o)),cat_L.id(cat_L.source(o)));
			
		}
	
		@Override
		public Arr source(Square<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(X);
			return f.getF();
		}
	
		@Override
		public Arr target(Square<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(X);
			return f.getG();
		}
	
		@Override
		public String showOb(Arr o) {
			if (o.trg().equals(X))
				return cat_L.showArr(o);
			else throw new IllegalArgumentException(o.label()+" is no Object in sliced category!");
		}
	
		@Override
		public String showArr(Square<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(X);
			return f.getF().label() + "-" + f.getM().label() + "-" + f.getG().label();
		}
}
