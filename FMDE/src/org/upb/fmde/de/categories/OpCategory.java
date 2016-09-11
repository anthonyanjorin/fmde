package org.upb.fmde.de.categories;

public class OpCategory<Ob, Arr> implements Category<Ob, Arr> {
	
	private Category<Ob, Arr> cat;
	
	public OpCategory(Category<Ob, Arr> cat) {
		this.cat = cat;
	}
	
	@Override
	public Arr compose(Arr f, Arr g) {
		return cat.compose(g, f);
	}

	@Override
	public Arr id(Ob f) {
		return cat.id(f);
	}

	@Override
	public Ob source(Arr f) {
		return cat.target(f);
	}

	@Override
	public Ob target(Arr g) {
		return cat.source(g);
	}

	@Override
	public String showOb(Ob o) {
		return cat.showOb(o);
	}

	@Override
	public String showArr(Arr a) {
		return "op_" + cat.showArr(a);
	}

}
