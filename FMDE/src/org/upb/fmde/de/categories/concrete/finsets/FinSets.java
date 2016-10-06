package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.LabelledCategory;

public class FinSets implements LabelledCategory<FinSet, TotalFunction> {
	
	public static FinSets FinSets = new FinSets(); 

	@Override
	public TotalFunction compose(TotalFunction f, TotalFunction g) {
		// TODO Compose arrows via function composition
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}

	@Override
	public TotalFunction id(FinSet a) {
		// TODO Create and return the id function: a -> a
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}
}
