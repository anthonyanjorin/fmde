package org.upb.fmde.de.categories.concrete.finsets;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.diagrams.Diagram;

public class CounterExampleChecker {
	public static boolean isCounterExampleForMono(Diagram<FinSet, TotalFunction> d){
		// Given: X -g-> Y -f-> Z
		//        X -h-> Y -f-> Z
		// Check: (g;f = h;f) ^ (g != h)
		TotalFunction g = d.getArrow("g");
		TotalFunction f = d.getArrow("f");
		TotalFunction h = d.getArrow("h");
		TotalFunction g_then_f = FinSets.compose(g, f);
		TotalFunction h_then_f = FinSets.compose(h, f);
		return g_then_f.isTheSameAs(h_then_f) && !(g.isTheSameAs(h));
	}
	
	public static boolean isCounterExampleForEpi(Diagram<FinSet, TotalFunction> d){
		// Given: Z -f-> Y -g-> X
		//        Z -f-> Y -h-> X
		// Check: (f;g = f;h) ^ (g != h)
		TotalFunction g = d.getArrow("g");
		TotalFunction f = d.getArrow("f");
		TotalFunction h = d.getArrow("h");
		TotalFunction f_then_g = FinSets.compose(f, g);
		TotalFunction f_then_h = FinSets.compose(f, h);
		return f_then_g.isTheSameAs(f_then_h) && !(g.isTheSameAs(h));
	}

}
