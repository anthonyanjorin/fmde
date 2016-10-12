package org.upb.fmde.de.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.GraphPatternMatcher;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher;
import org.upb.fmde.de.categories.concrete.tgraphs.Timer;
import org.upb.fmde.de.ecore.MetaMetaModelToGraphs;
import org.upb.fmde.de.ecore.MetaModelToGraphs;
import org.upb.fmde.de.ecore.ModelToGraphs;
import org.upb.fmde.de.ecore.ModelToTGraphs;
import org.upb.fmde.de.ecore.MultiplicitiesToGraphCondition;
import org.upb.fmde.de.graphconditions.And;
import org.upb.fmde.de.graphconditions.ComplexGraphCondition;
import org.upb.fmde.de.graphconditions.Constraint;
import org.upb.fmde.de.graphconditions.NegativeConstraint;
import org.upb.fmde.de.graphconditions.SatisfiableGraphCondition;
import org.upb.fmde.de.graphconditions.SimpleConstraint;

public class TestsEx3 {
	private static final String diagrams = "diagrams/ex3/";
	private static final BiFunction<TGraph, TGraph, PatternMatcher<TGraph, TGraphMorphism>> 
	create_pm = (_L, _G) -> new TPatternMatcher(_L, _G);
	
	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}
	
	@Test
	public void patternMatchingExamples() throws IOException {
		FinSet X = new FinSet("X", "a", "b");
		FinSet Y = new FinSet("Y", 1, 2, 3, 4);
		FinSetPatternMatcher fspm = new FinSetPatternMatcher(X, Y);
		int count = 0;
		for (TotalFunction m : fspm.getMonicMatches()) {
			FinSetDiagram d = new FinSetDiagram();
			d.objects(X, Y).arrows(m);
			d.prettyPrint(diagrams, "set_match_" + count++);
		}
		assertEquals(12, count);
		assertEquals(16, fspm.getMatches().size());
	
		Graph L = TestUtil.createPatternGraph("L");
		Graph G = TestUtil.createHostGraph("G");
		GraphPatternMatcher pm = new GraphPatternMatcher(L, G);
		count = 0;
		for (GraphMorphism m : pm.getMatches()) {
			GraphDiagram d = new GraphDiagram();
			d.objects(L, G).arrows(m);
			d.prettyPrint(diagrams, "graph_match_" + count++);
		}
		
	
		Graph TG = TestUtil.createListCardTypeGraph("TG");
		TGraph L_typed = TestUtil.createTypedPatternGraph(TG, L);
		TGraph G_typed = TestUtil.createTypedHostGraph(TG, G);
		TPatternMatcher pm_t = new TPatternMatcher(L_typed, G_typed);
		count = 0;
		for (TGraphMorphism m : pm_t.getMatches()) {
			TGraphDiagram d = new TGraphDiagram(TG);
			d.objects(L_typed, G_typed).arrows(m);
			d.prettyPrint(diagrams, "tgraph_match_" + count++);
		}		
	}

	@Test
	public void ecoreFormalisationAsGraphs() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
	
		{
			MetaMetaModelToGraphs importer = new MetaMetaModelToGraphs(EcorePackage.eINSTANCE, "Ecore");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			TestUtil.prettyPrintEcore(d, "Ecore", diagrams);
		}
	
		{
			EObject root = TestUtil.loadSimpleTrello(rs);
			MetaModelToGraphs importer = new MetaModelToGraphs(root, "SimpleTrello");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			TestUtil.prettyPrintEcore(d, "SimpleTrello", diagrams);
		}
	
		{
			EObject root = TestUtil.loadBoard(rs, "models/ex3/Board.xmi");
			ModelToGraphs importer = new ModelToGraphs(root, "G");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			TestUtil.prettyPrintEcore(d, "TrelloInstance", diagrams);
		}		
	}

	@Test
	public void ecoreFormalisationAsTypedGraphs() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);
		EObject root = TestUtil.loadBoard(rs, "models/ex3/Board.xmi");
		ModelToTGraphs importer = new ModelToTGraphs(root, "G");
		TGraphDiagram d = new TGraphDiagram(importer.getResult()[0].type().trg());
		d.objects(importer.getResult());
		TestUtil.prettyPrintEcore(d.getGraphDiagram(), "TrelloInstanceTyped", diagrams);
		d.getGraphDiagram().saveAsDot(diagrams, "TrelloInstanceTyped");		
	}

	@Test
	public void graphConditions() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);
		TGraph L = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/L.xmi", "L");
		TGraph P = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/P.xmi", "P");
		TGraph C1 = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/C1.xmi", "C1");
		TGraph C2 = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/C2.xmi", "C2");
		TGraph G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G");
		Graph TG = G.type().trg();
		
		TPatternMatcher pm = new TPatternMatcher(L, P);
		TGraphMorphism p = pm.getMonicMatches().get(0);
		p.label("p");
		
		pm = new TPatternMatcher(P, C1);
		TGraphMorphism c1 = pm.getMonicMatches().get(0);
		c1.label("c1");
		
		pm = new TPatternMatcher(P, C2);
		TGraphMorphism c2 = pm.getMonicMatches().get(0);
		c2.label("c2");
		
		TGraphDiagram d_gc = new TGraphDiagram(TG);
		d_gc.objects(L, P, C1, C2).arrows(p, c1, c2);
		TestUtil.prettyPrintTEcore(d_gc, "GraphCondition", diagrams);
		d_gc.saveAsDot(diagrams, "GraphCondition");
				
		SatisfiableGraphCondition<TGraph, TGraphMorphism> chosenMustHavePreviousOrNext = new SatisfiableGraphCondition<>(TGraphs.TGraphsFor(TG), p, Arrays.asList(c1, c2));
		pm = new TPatternMatcher(L, G);
		int counter = 0;
		Timer t = new Timer();
		t.tic();
		for (TGraphMorphism m : pm.getMatches()) {
			TGraphDiagram match = new TGraphDiagram(TG);
			match.objects(L, G).arrows(m);
			String label = "m_" + counter++;
			TestUtil.prettyPrintTEcore(match, label, diagrams);
			
			if(counter < 3)
				assertTrue(label + " fulfills the condition", chosenMustHavePreviousOrNext.isSatisfiedByArrow(m, create_pm));
			else
				assertFalse(label + "violates the condition", chosenMustHavePreviousOrNext.isSatisfiedByArrow(m, create_pm));
		}
		
		Constraint<TGraph, TGraphMorphism> mustHavePreviousOrNext = new Constraint<>(TGraphs.TGraphsFor(TG), P, Arrays.asList(c1, c2));
		assertFalse("Constraint is not satisfied", mustHavePreviousOrNext.isSatisfiedByObject(G, create_pm));
		
		SimpleConstraint<TGraph, TGraphMorphism> atLeastOneListWithACard = new SimpleConstraint<>(TGraphs.TGraphsFor(TG), P);
		assertTrue("Simple constraint is satisfied", atLeastOneListWithACard.isSatisfiedByObject(G, create_pm));
		assertFalse("Simple constraint is not satisfied", atLeastOneListWithACard.isSatisfiedByObject(L, create_pm));
		
		TGraph N = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/N.xmi", "N");
		TGraph G_ = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/BoardWithTreeStructure.xmi", "G'");
		NegativeConstraint<TGraph, TGraphMorphism> linearNextPrev = new NegativeConstraint<>(TGraphs.TGraphsFor(TG), N);
		assertTrue("Negative constraint is satisfied", linearNextPrev.isSatisfiedByObject(G, create_pm));
		assertFalse("Negative constraint is not satisfied", linearNextPrev.isSatisfiedByObject(G_, create_pm));
		
		ComplexGraphCondition<TGraph, TGraphMorphism> complexgc = new And<TGraph, TGraphMorphism>(linearNextPrev, atLeastOneListWithACard, mustHavePreviousOrNext);
		assertFalse("Complex graph condition is not satisfied", complexgc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG).initialObject().up.apply(G), create_pm));
	
		ComplexGraphCondition<TGraph, TGraphMorphism> anothercomplexgc = new And<TGraph, TGraphMorphism>(linearNextPrev, atLeastOneListWithACard);
		assertTrue("Complex graph condition is satisfied", anothercomplexgc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG).initialObject().up.apply(G), create_pm));
		
		t.toc();		
	}

	@Test
	public void multiplicities() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		EObject root = TestUtil.loadSimpleTrello(rs);
		MetaModelToGraphs importer = new MetaModelToGraphs(root, "SimpleTrello");
		Graph TG = importer.getResult();
		MultiplicitiesToGraphCondition mp2gc = new MultiplicitiesToGraphCondition(TG);
		ComplexGraphCondition<TGraph, TGraphMorphism> gc = mp2gc.createGraphConditionFromMultiplicities();
		
		TGraph G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G");
		boolean sat = gc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG).initialObject().up.apply(G), create_pm);
		assertTrue("Multiplicities must be satisfied", sat);
		
		TGraph G_empty = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/EmptyBoard.xmi", "G");
		sat = gc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG).initialObject().up.apply(G_empty), create_pm);
		assertFalse("Multiplicities must not be satisfied", sat);		
	}
}
