package org.upb.fmde.de.tests;

import static org.junit.Assert.assertTrue;
import static org.upb.fmde.de.categories.concrete.finsets.OpFinSets.OpFinSets;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.concrete.finsets.CounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.OpCounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class TestsEx1 {
	private static final String diagrams = "diagrams/ex1/";
	
	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void monicExample() throws IOException {
		Diagram<FinSet, TotalFunction> d = createDiagram1();

		d.saveAsDot(diagrams, "monicExample")
		 .prettyPrint(diagrams, "monicExample");
		
		assertTrue(CounterExampleChecker.isCounterExampleForMono(d));	
	}
	
	@Test
	public void epicExample() throws IOException {
		FinSetDiagram d = createDiagram2(); 
		
		 d.saveAsDot(diagrams, "epicExample")
		  .prettyPrint(diagrams, "epicExample");
		
		assertTrue(CounterExampleChecker.isCounterExampleForEpi(d));
	}
	
	@Test
	public void dualityExample() throws IOException {
		FinSetDiagram d1 = createDiagram1();
		Diagram<FinSet, TotalFunction> opd1 = new Diagram<FinSet, TotalFunction>(OpFinSets)
				.arrows(d1.getArrows())
				.objects(d1.getObjects());
		
		opd1.saveAsDot(diagrams, "dualityExampleEpi");
		
		assertTrue(OpCounterExampleChecker.isCounterExampleForEpi(opd1));
		
		FinSetDiagram d2 = createDiagram2();
		Diagram<FinSet, TotalFunction> opd2 = new Diagram<FinSet, TotalFunction>(OpFinSets)
				.arrows(d2.getArrows())
				.objects(d2.getObjects());
		
		opd2.saveAsDot(diagrams, "dualityExampleMono");
		
		assertTrue(OpCounterExampleChecker.isCounterExampleForMono(opd2));
	}
	
	private FinSetDiagram createDiagram1() {
		FinSet X = new FinSet("X", "a", "b", "c");
		FinSet Y = new FinSet("Y", 1, 2, "blup");
		FinSet Z = new FinSet("Z", 'a', 3, "foo");
		
		TotalFunction g = new TotalFunction(X, "g", Y)
				.addMapping(X.get("a"), Y.get("1"))
				.addMapping(X.get("b"), Y.get("2"))
				.addMapping(X.get("c"), Y.get("1"));
		
		TotalFunction h = new TotalFunction(X, "h", Y)
				.addMapping(X.get("a"), Y.get("1"))
				.addMapping(X.get("b"), Y.get("2"))
				.addMapping(X.get("c"), Y.get("blup"));
		
		TotalFunction f = new TotalFunction(Y, "f", Z)
				.addMapping(Y.get("1"), Z.get("3"))
				.addMapping(Y.get("2"), Z.get("a"))
				.addMapping(Y.get("blup"), Z.get("3"));
				
		FinSetDiagram d1 = new FinSetDiagram();
		d1.objects(X, Y, Z).arrows(g, h, f);

		return d1;
	}

	private FinSetDiagram createDiagram2() {
		FinSet Z = new FinSet("Z", "a", "b", "c");
		FinSet Y = new FinSet("Y", 1, 2, "blup");
		FinSet X = new FinSet("X", 'a', 3, "foo");
		
		TotalFunction g = new TotalFunction(Y, "g", X)
				.addMapping(Y.get("1"), X.get("a"))
				.addMapping(Y.get("2"), X.get("3"))
				.addMapping(Y.get("blup"), X.get("foo"));
		
		TotalFunction h = new TotalFunction(Y, "h", X)
				.addMapping(Y.get("1"), X.get("3"))
				.addMapping(Y.get("2"), X.get("a"))
				.addMapping(Y.get("blup"), X.get("foo"));
		
		TotalFunction f = new TotalFunction(Z, "f", Y)
				.addMapping(Z.get("a"), Y.get("blup"))
				.addMapping(Z.get("b"), Y.get("blup"))
				.addMapping(Z.get("c"), Y.get("blup"));
				
		FinSetDiagram d2 = new FinSetDiagram();
		d2.objects(Z, Y, Z).arrows(g, h, f);

		return d2;
	}
}
