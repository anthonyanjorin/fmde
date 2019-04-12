package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class CounterExampleChecker {
	public static <O, A extends ComparableArrow<A>> boolean isCounterExampleForMono(Diagram<O, A> d) {
		// Given: X -g-> Y -f-> Z
		// X -h-> Y -f-> Z
		// Check: (g;f = h;f) ^ (g != h)
		var g = d.getArrow("g");
		var f = d.getArrow("f");
		var h = d.getArrow("h");

		var cat = d.getCat();

		try {
			return cat.compose(g, f).isTheSameAs(cat.compose(h, f)) && !g.isTheSameAs(h);
		} catch (Exception e) {
			return false;
		}
	}

	public static <O, A extends ComparableArrow<A>> boolean isCounterExampleForEpi(Diagram<O, A> d) {
		// Given: Z -f-> Y -g-> X
		// Z -f-> Y -h-> X
		// Check: (f;g = f;h) ^ (g != h)
		var g = d.getArrow("g");
		var f = d.getArrow("f");
		var h = d.getArrow("h");

		var cat = d.getCat();

		try {
			return cat.compose(f, g).isTheSameAs(cat.compose(f, h)) && !g.isTheSameAs(h);
		} catch (Exception e) {
			return false;
		}
	}
}
