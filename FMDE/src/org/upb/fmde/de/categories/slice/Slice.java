package org.upb.fmde.de.categories.slice;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;

public class Slice<Ob, Arr extends LabelledArrow<Ob>> implements Category<Arr, Triangle<Ob, Arr>>{
	
	protected Ob T;
	protected Category<Ob, Arr> cat;
	
	public Slice(Category<Ob, Arr> cat, Ob T) {
		this.cat = cat;
		this.T = T;
	}

	@Override
		public Triangle<Ob, Arr> compose(Triangle<Ob, Arr> f, Triangle<Ob, Arr> g) {
			((Triangle<Ob,Arr>)f).checkValidity(T);
			((Triangle<Ob,Arr>)g).checkValidity(T);
			return new Triangle<Ob, Arr>(f.label()+";"+g.label(),cat.compose(f.getF(), g.getF()),f.getType(),g.getType_());
		}
	
		@Override
		public Triangle<Ob, Arr> id(Arr o) {
			return new Triangle<Ob, Arr>("id_"+o.label(),cat.id(cat.source(o)),cat.id(cat.source(o)),cat.id(cat.source(o)));
			
		}
	
		@Override
		public Arr source(Triangle<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(T);
			return f.getType();
		}
	
		@Override
		public Arr target(Triangle<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(T);
			return f.getType_();
		}
		
		@Override
		public String showOb(Arr o) {
			if (o.trg().equals(T))
				return cat.showArr(o);
			else throw new IllegalArgumentException(o.label()+" is no Object in sliced category!");
		}
	
		@Override
		public String showArr(Triangle<Ob, Arr> f) {
			((Triangle<Ob,Arr>)f).checkValidity(T);
			return f.getType().label() + "-" + f.getF().label() + "-" + f.getType_().label();
		}
}
