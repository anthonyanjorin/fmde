package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class CounterExampleChecker {
	public static <O, A extends ComparableArrow<A>> boolean isCounterExampleForMono(Diagram<O, A> d) {
		// TODO (4) Implement generic check for mono counter example
		// Given: X -g-> Y -f-> Z
		// X -h-> Y -f-> Z
		// Check: (g;f = h;f) ^ (g != h)
		var g = d.getArrow("g");
		var f = d.getArrow("f");
		var h = d.getArrow("h");

		var cat = d.getCat();

		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}

	public static <O, A extends ComparableArrow<A>> boolean isCounterExampleForEpi(Diagram<O, A> d) {
		// TODO (5) Implement generic check for epi counter example
		// Given: Z -f-> Y -g-> X
		// Z -f-> Y -h-> X
		// Check: (f;g = f;h) ^ (g != h)
		var g = d.getArrow("g");
		var f = d.getArrow("f");
		var h = d.getArrow("h");

		var cat = d.getCat();

		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}

}
