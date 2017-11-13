package org.upb.fmde.de.categories.comma;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;

public class Slice<Ob, Arr extends LabelledArrow<Ob>> extends Comma<Ob, Arr, Ob, Arr> implements Category<Arr, Square<Ob, Arr>>{
	
	private Ob X;
	
	public Slice(Category<Ob, Arr> cat, Ob X) {
		super(cat, cat);
	}

	@Override
		public Triangle<Ob, Arr> compose(Square<Ob, Arr> f, Square<Ob, Arr> g) {
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
