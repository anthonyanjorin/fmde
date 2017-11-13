package org.upb.fmde.de.categories.comma;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;

public class Comma<Ob_L, Arr_L extends LabelledArrow<Ob_L>, Ob_R, Arr_R extends LabelledArrow<Ob_R>> /*implements Category<Arr_L, Square<Ob_L, Arr_L>>*/ {
	
	protected Category<Ob_L, Arr_L> cat_L;
	protected Category<Ob_R, Arr_R> cat_R;
	
	public Comma(Category<Ob_L, Arr_L> cat_L, Category<Ob_R, Arr_R> cat_R) {
		this.cat_L = cat_L;
		this.cat_R = cat_R;
	}

	public Square<Ob_L, Arr_L> compose(Square<Ob_L, Arr_L> f, Square<Ob_R, Arr_R> g) {
		f.checkValidity();
		g.checkValidity();
		return new Square<Ob_L, Arr_L>(f.label()+";"+g.label(), cat_L.compose(f.getF(), g.getF()), f.getM(), g.getM()),g.getG(),cat_R.compose(f.getN(), g.getN()));
	}

	@Override
	public Square<Ob_L, Arr_L> id(Arr_L o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arr_L source(Square<Ob_L, Arr_L> f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arr_L target(Square<Ob_L, Arr_L> f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String showOb(Arr_L o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String showArr(Square<Ob_L, Arr_L> f) {
		// TODO Auto-generated method stub
		return null;
	}

}
