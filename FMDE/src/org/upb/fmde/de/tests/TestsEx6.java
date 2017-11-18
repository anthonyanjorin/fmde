package org.upb.fmde.de.tests;

import static org.junit.Assert.assertTrue;
import static org.upb.fmde.de.categories.concrete.finsets.OpFinSets.OpFinSets;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
//import org.upb.fmde.de.categories.concrete.finsets.CounterExampleChecker;
//import org.upb.fmde.de.categories.concrete.finsets.FinSet;
//import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
//import org.upb.fmde.de.categories.concrete.finsets.OpCounterExampleChecker;
//import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.slice.*;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class TestsEx6 {
/*	private static final String diagrams = "diagrams/ex6/";
	
	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void simpleExample() throws IOException {
		Diagram<FinSet, TotalFunction> d = createDiagram1();

		d.saveAsDot(diagrams, "simpleExample")
		 .prettyPrint(diagrams, "simpleExample");
		
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

	private SliceDiagram<Graph, GraphMorphism> createDiagram2() {
		
		// left-hand side
		FinSet V_L = new FinSet("V", "L", "C");
		FinSet E_L = new FinSet("E", "LC");
		TotalFunction src_L = new TotalFunction(E_L, "src", V_L)
				.addMapping(E_L.get("LC"), V_L.get("L"));
		TotalFunction trg_L = new TotalFunction(E_L, "trg", V_L)
				.addMapping(E_L.get("LC"), V_L.get("C"));
		Graph L = new Graph("L", V_L, E_L, src_L, trg_L);
		
		// right-hand side
		FinSet V_R = new FinSet("V", "L", "C", "D");
		FinSet E_R= new FinSet("E", "LC", "LD");
		TotalFunction src_R = new TotalFunction(E_R, "src", V_R)
				.addMapping(E_R.get("LC"), V_R.get("L"))
				.addMapping(E_R.get("LD"), V_R.get("L"));
		TotalFunction trg_R = new TotalFunction(E_R, "trg", V_R)
				.addMapping(E_R.get("LC"), V_R.get("C"))
				.addMapping(E_R.get("LD"), V_R.get("D"));
		Graph R = new Graph("R", V_R, E_R, src_R, trg_R);		
		
		// host graph
		FinSet V_G = new FinSet("V", "L", "C", "M");
		FinSet E_G = new FinSet("E", "LC");
		TotalFunction src_G = new TotalFunction(E_G, "src", V_G)
				.addMapping(E_G.get("LC"), V_G.get("L"));
		TotalFunction trg_G = new TotalFunction(E_G, "trg", V_G)
				.addMapping(E_G.get("LC"), V_G.get("C"));
		Graph G = new Graph("G", V_G, E_G, src_G, trg_G);
		
		// target graph
		FinSet V_H = new FinSet("V", "L", "C", "D", "M");
		FinSet E_H= new FinSet("E", "LC", "LD");
		TotalFunction src_H = new TotalFunction(E_H, "src", V_H)
				.addMapping(E_H.get("LC"), V_H.get("L"))
				.addMapping(E_H.get("LD"), V_H.get("L"));
		TotalFunction trg_H = new TotalFunction(E_H, "trg", V_H)
				.addMapping(E_H.get("LC"), V_H.get("C"))
				.addMapping(E_H.get("LD"), V_H.get("D"));
		Graph H = new Graph("H", V_H, E_H, src_H, trg_H);	
		
		// type graph
		FinSet V_Type = new FinSet("V", "List", "Card");
		FinSet E_Type = new FinSet("E", "ListCard");
		TotalFunction src_Type = new TotalFunction(E_Type, "src", V_Type)
				.addMapping(E_Type.get("ListCard"), V_Type.get("List"));
		TotalFunction trg_Type = new TotalFunction(E_Type, "trg", V_L)
				.addMapping(E_Type.get("ListCard"), V_Type.get("Card"));
		Graph Type = new Graph("L", V_Type, E_Type, src_Type, trg_Type);
		
		// Rule
		TotalFunction r_V = new TotalFunction(V_L, "r", V_R)
				.addMapping(V_L.get("L"), V_R.get("L"))
				.addMapping(V_L.get("C"), V_R.get("C"));
		TotalFunction r_E = new TotalFunction(E_L, "r", E_R)
				.addMapping(E_L.get("LC"), E_R.get("LC"));
		GraphMorphism r = new GraphMorphism("r", L, R, r_E, r_V);
		
		// Match
		TotalFunction m_V = new TotalFunction(V_L, "m", V_G)
				.addMapping(V_L.get("L"), V_G.get("L"))
				.addMapping(V_L.get("C"), V_G.get("C"));
		TotalFunction m_E = new TotalFunction(E_L, "m", E_G)
				.addMapping(E_L.get("LC"), E_G.get("LC"));
		GraphMorphism m = new GraphMorphism("m", L, G, m_E, m_V);
		
		// Rule application
		TotalFunction r__V = new TotalFunction(V_G, "r'", V_H)
				.addMapping(V_G.get("L"), V_H.get("L"))
				.addMapping(V_G.get("C"), V_H.get("C"))
				.addMapping(V_G.get("M"), V_H.get("M"));
		TotalFunction r__E = new TotalFunction(E_G, "r'", E_H)
				.addMapping(E_G.get("LC"), E_H.get("LC"));
		GraphMorphism r_ = new GraphMorphism("r'", G, H, r__E, r__V);
		
		// Co-Match
		TotalFunction m__V = new TotalFunction(V_R, "m'", V_H)
				.addMapping(V_R.get("L"), V_H.get("L"))
				.addMapping(V_R.get("C"), V_H.get("C"));
		TotalFunction m__E = new TotalFunction(E_R, "m'", E_H)
				.addMapping(E_R.get("LC"), E_H.get("LC"));
		GraphMorphism m_ = new GraphMorphism("m'", R, H, m__E, m__V);
		
		SliceDiagram<Graph, GraphMorphism> d2 = new SliceDiagram(Graphs.Graphs);
		
	}*/
}
