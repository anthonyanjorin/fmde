package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.diagrams.Diagram;

public class CounterExampleChecker {
	public static boolean isCounterExampleForMono(Diagram<FinSet, TotalFunction> d){
		// TODO (4) Implement check for mono counter example
		// Given: X -g-> Y -f-> Z
		//        X -h-> Y -f-> Z
		// Check: (g;f = h;f) ^ (g != h)
		TotalFunction g = d.getArrow("g");
		TotalFunction f = d.getArrow("f");
		TotalFunction h = d.getArrow("h");
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}
	
	public static boolean isCounterExampleForEpi(Diagram<FinSet, TotalFunction> d){
		// TODO (5) Implement check for epi counter example
		// Given: Z -f-> Y -g-> X
		//        Z -f-> Y -h-> X
		// Check: (f;g = f;h) ^ (g != h)
		TotalFunction g = d.getArrow("g");
		TotalFunction f = d.getArrow("f");
		TotalFunction h = d.getArrow("h");
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}

}
