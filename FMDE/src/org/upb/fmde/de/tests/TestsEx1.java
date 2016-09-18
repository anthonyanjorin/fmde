package org.upb.fmde.de.tests;

import static org.upb.fmde.de.categories.concrete.finsets.OpFinSets.OpFinSets;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.finsets.CounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.OpCounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class TestsEx1 {
	private static final String diagrams = "diagrams/ex1/";
	
	public static void main(String[] args) throws IOException {
		for(File f : new File(diagrams).listFiles()) f.delete();
		
		monicExample();
		epicExample();
		dualityExample();		
		
		System.out.println("All diagrams created.");
	}

	private static void dualityExample() throws IOException {
		FinSetDiagram d1 = createDiagram1();
		Diagram<FinSet, TotalFunction> opd1 = new Diagram<FinSet, TotalFunction>(OpFinSets)
				.arrows(d1.getArrows())
				.objects(d1.getObjects())
				.saveAsDot(diagrams, "epi_diagram_in_OpFinSets");
		
		System.out.println("op_f is not epic? " + (OpCounterExampleChecker.isCounterExampleForEpi(opd1)? "Yes" : "I don't know"));
		
		FinSetDiagram d2 = createDiagram2();
		Diagram<FinSet, TotalFunction> opd2 = new Diagram<FinSet, TotalFunction>(OpFinSets)
				.arrows(d2.getArrows())
				.objects(d2.getObjects())
				.saveAsDot(diagrams, "mono_diagram_in_OpFinSets");
		
		System.out.println("op_f is not monic? " + (OpCounterExampleChecker.isCounterExampleForMono(opd2)? "Yes" : "I don't know"));
	}

	private static void monicExample() throws IOException {
		Diagram<FinSet, TotalFunction> d1 = createDiagram1()
				.saveAsDot(diagrams, "mono_diagram_in_FinSets");
		
		System.out.println("f is not monic? " + (CounterExampleChecker.isCounterExampleForMono(d1)? "Yes" : "I don't know"));
	}
	
	private static FinSetDiagram createDiagram1() {
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
				
		return (FinSetDiagram) new FinSetDiagram().objects(X, Y, Z).arrows(g, h, f);
	}

	private static void epicExample() throws IOException {
		FinSetDiagram d2 = createDiagram2(); 
		d2.saveAsDot(diagrams, "epi_diagram_in_FinSets");
		
		System.out.println("f is not epic? " + (CounterExampleChecker.isCounterExampleForEpi(d2)? "Yes" : "I don't know"));
	}

	private static FinSetDiagram createDiagram2() {
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
