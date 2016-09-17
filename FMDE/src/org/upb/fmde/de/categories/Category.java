package org.upb.fmde.de.categories;

public interface Category<Ob, Arr> {
	Arr compose(Arr f, Arr g);
	Arr id(Ob o);
	
	Ob source(Arr f);
	Ob target(Arr f); 
	
	String showOb(Ob o);
	String showArr(Arr f);
	
	static void ensure(boolean condition, String message) {
		if(!condition){
			throw new IllegalStateException(message);
		}
	}
}