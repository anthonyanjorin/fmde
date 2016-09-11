package org.upb.fmde.de.categories;

public interface LabelledCategory<Ob extends Labelled, Arr extends LabelledArrow<Ob>> extends Category<Ob, Arr> {

	@Override
	default String showArr(Arr a) {
		return a.label;
	}
	
	@Override
	default String showOb(Ob o) {
		return o.label;
	}
	
	@Override
	default Ob source(Arr f) {
		return f.source;
	}
	
	@Override
	default Ob target(Arr f) {
		return f.target;
	}
}
