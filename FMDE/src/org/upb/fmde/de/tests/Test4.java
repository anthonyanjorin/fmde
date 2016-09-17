package org.upb.fmde.de.tests;

import java.io.IOException;

import org.junit.Test;
import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.finsets.FinSets;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.pushouts.CoSpan;
import org.upb.fmde.de.categories.pushouts.Span;

public class Test4 {
	private static final String diagrams = "diagrams/ex4/";

	@Test
	public void test1() throws IOException {
		FinSet L = new FinSet("L", "existingCard");
		FinSet R = new FinSet("R", "existingCard", "buy beer");
		FinSet G = new FinSet("G", "install new sink", "buy some paint");
		
		TotalFunction r = new TotalFunction(L, "r", R).addMapping(L.get("existingCard"), R.get("existingCard"));
		TotalFunction m = new TotalFunction(L, "m", G).addMapping(L.get("existingCard"), G.get("install new sink"));
		
		CoSpan<TotalFunction> pushout = FinSets.FinSets.pushout(new Span<TotalFunction>(FinSets.FinSets, r, m));
		
		new FinSetDiagram()
				.objects(L, R, G, pushout.horizontal.getTarget())
				.arrows(pushout.horizontal, pushout.vertical, r, m)
				.prettyPrint(diagrams, "poFinSets");
	}

}
