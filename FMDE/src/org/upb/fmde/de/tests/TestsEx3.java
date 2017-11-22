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
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher;
import org.upb.fmde.de.categories.concrete.tgraphs.Timer;
import org.upb.fmde.de.categories.slice.Triangle;
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
	private static final BiFunction<GraphMorphism, GraphMorphism, PatternMatcher<GraphMorphism, Triangle<Graph, GraphMorphism>>> 
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
		GraphMorphism L_typed = TestUtil.createTypedPatternGraph(TG, L);
		GraphMorphism G_typed = TestUtil.createTypedHostGraph(TG, G);
		TPatternMatcher pm_t = new TPatternMatcher(L_typed, G_typed);
		count = 0;
		for (Triangle<Graph, GraphMorphism> m : pm_t.getMatches()) {
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
		TGraphDiagram d = new TGraphDiagram(importer.getResult()[0].trg());
		d.objects(importer.getResult());
		TestUtil.prettyPrintEcore(d.getGraphDiagram(), "TrelloInstanceTyped", diagrams);
		d.getGraphDiagram().saveAsDot(diagrams, "TrelloInstanceTyped");		
	}

	@Test
	public void graphConditions() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);
		
		GraphMorphism[] L_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex3/graphCondition/L.xmi", "L");
		GraphMorphism Ecore = L_TG_Ecore[2];
		GraphMorphism TG = L_TG_Ecore[1];

		GraphMorphism L = L_TG_Ecore[0];
		GraphMorphism P = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/P.xmi", "P", TG, Ecore);
		GraphMorphism C1 = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/C1.xmi", "C1", TG, Ecore);
		GraphMorphism C2 = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/C2.xmi", "C2", TG, Ecore);
		GraphMorphism G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G", TG, Ecore);
		
		TPatternMatcher pm = new TPatternMatcher(L, P);
		Triangle<Graph, GraphMorphism> p = pm.getMonicMatches().get(0);
		p.label("p");
		
		pm = new TPatternMatcher(P, C1);
		Triangle<Graph, GraphMorphism> c1 = pm.getMonicMatches().get(0);
		c1.label("c1");
		
		pm = new TPatternMatcher(P, C2);
		Triangle<Graph, GraphMorphism> c2 = pm.getMonicMatches().get(0);
		c2.label("c2");
		
		TGraphDiagram d_gc = new TGraphDiagram(TG.src());
		d_gc.objects(L, P, C1, C2).arrows(p, c1, c2);
		TestUtil.prettyPrintTEcore(d_gc, "GraphCondition", diagrams);
		d_gc.saveAsDot(diagrams, "GraphCondition");
				
		SatisfiableGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> chosenMustHavePreviousOrNext = new SatisfiableGraphCondition(TGraphs.TGraphsFor(TG.src()), p, Arrays.asList(c1, c2));
		pm = new TPatternMatcher(L, G);
		int counter = 0;
		Timer t = new Timer();
		t.tic();
		for (Triangle<Graph, GraphMorphism> m : pm.getMatches()) {
			TGraphDiagram match = new TGraphDiagram(TG.src());
			match.objects(L, G).arrows(m);
			String label = "m_" + counter++;
			TestUtil.prettyPrintTEcore(match, label, diagrams);
			
			if(counter < 3)
				assertTrue(label + " fulfills the condition", chosenMustHavePreviousOrNext.isSatisfiedByArrow(m, create_pm));
			else
				assertFalse(label + " violates the condition", chosenMustHavePreviousOrNext.isSatisfiedByArrow(m, create_pm));
		}
		
		Constraint<GraphMorphism, Triangle<Graph, GraphMorphism>> mustHavePreviousOrNext = new Constraint(TGraphs.TGraphsFor(TG.src()), P, Arrays.asList(c1, c2));
		assertFalse("Constraint is not satisfied", mustHavePreviousOrNext.isSatisfiedByObject(G, create_pm));
		
		SimpleConstraint<GraphMorphism, Triangle<Graph, GraphMorphism>> atLeastOneListWithACard = new SimpleConstraint<>(TGraphs.TGraphsFor(TG.src()), P);
		assertTrue("Simple constraint is satisfied", atLeastOneListWithACard.isSatisfiedByObject(G, create_pm));
		assertFalse("Simple constraint is not satisfied", atLeastOneListWithACard.isSatisfiedByObject(L, create_pm));
		
		GraphMorphism N = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/N.xmi", "N", TG, Ecore);
		GraphMorphism G_ = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/BoardWithTreeStructure.xmi", "G'", TG, Ecore);
		NegativeConstraint<GraphMorphism, Triangle<Graph, GraphMorphism>> linearNextPrev = new NegativeConstraint<>(TGraphs.TGraphsFor(TG.src()), N);
		assertTrue("Negative constraint is satisfied", linearNextPrev.isSatisfiedByObject(G, create_pm));
		assertFalse("Negative constraint is not satisfied", linearNextPrev.isSatisfiedByObject(G_, create_pm));
		
		ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> complexgc = new And<GraphMorphism, Triangle<Graph, GraphMorphism>>(linearNextPrev, atLeastOneListWithACard, mustHavePreviousOrNext);
		assertFalse("Complex graph condition is not satisfied", complexgc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG.src()).initialObject().up.apply(G), create_pm));
	
		ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> anothercomplexgc = new And<GraphMorphism, Triangle<Graph, GraphMorphism>>(linearNextPrev, atLeastOneListWithACard);
		assertTrue("Complex graph condition is satisfied", anothercomplexgc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG.src()).initialObject().up.apply(G), create_pm));
		
		t.toc();		
	}

	@Test
	public void multiplicities() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);

		GraphMorphism[] G_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex3/Board.xmi", "G");
		GraphMorphism Ecore = G_TG_Ecore[2];
		GraphMorphism TG = G_TG_Ecore[1];
		GraphMorphism G = G_TG_Ecore[0];
		
		MultiplicitiesToGraphCondition mp2gc = new MultiplicitiesToGraphCondition(TG.src());
		ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> gc = mp2gc.createGraphConditionFromMultiplicities();
		
		boolean sat = gc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG.src()).initialObject().up.apply(G), create_pm);
		assertTrue("Multiplicities must be satisfied", sat);
		
		GraphMorphism G_empty = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/EmptyBoard.xmi", "G", TG, Ecore);
		sat = gc.isSatisfiedByArrow(TGraphs.TGraphsFor(TG.src()).initialObject().up.apply(G_empty), create_pm);
		assertFalse("Multiplicities must not be satisfied", sat);		
	}
}


