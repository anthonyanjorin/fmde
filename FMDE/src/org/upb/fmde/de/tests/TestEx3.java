package org.upb.fmde.de.tests;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphDiagram;
import org.upb.fmde.de.categories.graphs.GraphMorphism;
import org.upb.fmde.de.categories.graphs.GraphPatternMatcher;
import org.upb.fmde.de.categories.tgraphs.TGraph;
import org.upb.fmde.de.categories.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.tgraphs.TGraphs;
import org.upb.fmde.de.categories.tgraphs.TPatternMatcher;
import org.upb.fmde.de.categories.tgraphs.Timer;
import org.upb.fmde.de.ecore.EcorePrinter;
import org.upb.fmde.de.ecore.MetaMetaModelToGraphs;
import org.upb.fmde.de.ecore.MetaModelToGraphs;
import org.upb.fmde.de.ecore.ModelToGraphs;
import org.upb.fmde.de.ecore.ModelToTGraphs;
import org.upb.fmde.de.ecore.TEcorePrinter;
import org.upb.fmde.de.graphconditions.SatisfiableGraphCondition;

public class TestEx3 {
	private static final String diagrams = "diagrams/ex3/";

	public static void main(String[] args) throws IOException {
		for (File f : new File(diagrams).listFiles())
			f.delete();

		patternMatchingExamples();
		ecoreFormalisationAsGraphs();
		ecoreFormalisationAsTypedGraphs();
		graphConditions();

		// TODO: Program all simplifications of graph conditions
		// TODO: Convert multiplicities to domain constraints

		System.out.println("All diagrams created");
	}

	private static void graphConditions() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		loadSimpleTrello(rs);
		TGraph L = loadBoardAsTGraph(rs, "models/ex3/graphCondition/L.xmi", "L");
		TGraph P = loadBoardAsTGraph(rs, "models/ex3/graphCondition/P.xmi", "P");
		TGraph C1 = loadBoardAsTGraph(rs, "models/ex3/graphCondition/C1.xmi", "C1");
		TGraph C2 = loadBoardAsTGraph(rs, "models/ex3/graphCondition/C2.xmi", "C2");
		TGraph G = loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G");
		
		TPatternMatcher pm = new TPatternMatcher(L, P);
		TGraphMorphism p = pm.getMonicMatches().get(0);
		p.label("p");
		
		pm = new TPatternMatcher(P, C1);
		TGraphMorphism c1 = pm.getMonicMatches().get(0);
		c1.label("c1");
		
		pm = new TPatternMatcher(P, C2);
		TGraphMorphism c2 = pm.getMonicMatches().get(0);
		c2.label("c2");
		
		TGraphDiagram d_gc = new TGraphDiagram();
		d_gc.objects(L, P, C1, C2).arrows(p, c1, c2);
		prettyPrintTEcore(d_gc, "GraphCondition");
		d_gc.saveAsDot(diagrams, "GraphCondition");
		
		SatisfiableGraphCondition<TGraph, TGraphMorphism> mustHavePreviousOrNext = new SatisfiableGraphCondition<>(TGraphs.TGraphs, p, Arrays.asList(c1, c2));
		pm = new TPatternMatcher(L, G);
		int counter = 0;
		
		Timer t = new Timer();
		t.tic();
		for (TGraphMorphism m : pm.getMatches()) {		
			TGraphDiagram match = new TGraphDiagram();
			match.objects(L, G).arrows(m);
			String label = "m_" + counter++;
			prettyPrintTEcore(match, label);
			System.out.println(label + ": " + mustHavePreviousOrNext.isSatisfiedBy(m, (_L, _G) -> new TPatternMatcher(_L, _G)));
		}
		t.toc();
	}

	private static TGraph loadBoardAsTGraph(ResourceSet rs, String fileName, String label) throws IOException {
		EObject o = loadBoard(rs, fileName);
		ModelToTGraphs importer = new ModelToTGraphs(o, label);
		return importer.getResult()[0];
	}

	private static void ecoreFormalisationAsTypedGraphs() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		loadSimpleTrello(rs);
		EObject root = loadBoard(rs, "models/ex3/Board.xmi");
		ModelToTGraphs importer = new ModelToTGraphs(root, "G");
		TGraphDiagram d = new TGraphDiagram();
		d.objects(importer.getResult());
		prettyPrintEcore(d.getGraphDiagram(), "TrelloInstanceTyped");
		d.getGraphDiagram().saveAsDot(diagrams, "TrelloInstanceTyped");
	}

	private static void prettyPrintEcore(GraphDiagram d, String label) throws IOException {
		d.saveAsDot(new File(diagrams + label + ".ecore.plantuml"), new EcorePrinter(d));
	}
	
	private static void prettyPrintTEcore(TGraphDiagram d, String label) throws IOException {
		d.saveAsDot(new File(diagrams + label + ".ecore.plantuml"), new TEcorePrinter(d));
	}

	private static void ecoreFormalisationAsGraphs() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();

		{
			MetaMetaModelToGraphs importer = new MetaMetaModelToGraphs(EcorePackage.eINSTANCE, "Ecore");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			prettyPrintEcore(d, "Ecore");
		}

		{
			EObject root = loadSimpleTrello(rs);
			MetaModelToGraphs importer = new MetaModelToGraphs(root, "SimpleTrello");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			prettyPrintEcore(d, "SimpleTrello");
		}

		{
			EObject root = loadBoard(rs, "models/ex3/Board.xmi");
			ModelToGraphs importer = new ModelToGraphs(root, "G");
			GraphDiagram d = new GraphDiagram();
			d.objects(importer.getResult());
			prettyPrintEcore(d, "TrelloInstance");
		}
	}

	private static EObject loadBoard(ResourceSet rs, String fileName) throws IOException {
		Resource r = rs.createResource(URI.createFileURI(fileName));
		r.load(null);
		EObject root = r.getContents().get(0);
		return root;
	}

	private static EObject loadSimpleTrello(ResourceSet rs) throws IOException {
		Resource r = rs.createResource(URI.createFileURI("models/ex3/SimpleTrello.ecore"));
		r.load(null);
		r.setURI(URI.createURI("platform:/resource/FMDE/models/ex3/SimpleTrello.ecore"));
		EObject root = r.getContents().get(0);
		return root;
	}

	private static void patternMatchingExamples() throws IOException {
		FinSet X = new FinSet("X", "a", "b");
		FinSet Y = new FinSet("Y", 1, 2, 3, 4);
		FinSetPatternMatcher fspm = new FinSetPatternMatcher(X, Y);
		int count = 0;
		for (TotalFunction m : fspm.getMonicMatches()) {
			FinSetDiagram d = new FinSetDiagram();
			d.objects(X, Y).arrows(m);
			d.prettyPrint(diagrams, "set_match_" + count++);
		}

		Graph L = TestsEx2.createPatternGraph("L");
		Graph G = TestsEx2.createHostGraph("G");
		GraphPatternMatcher pm = new GraphPatternMatcher(L, G);
		count = 0;
		for (GraphMorphism m : pm.getMatches()) {
			GraphDiagram d = new GraphDiagram();
			d.objects(L, G).arrows(m);
			d.prettyPrint(diagrams, "graph_match_" + count++);
		}

		Graph TG = TestsEx2.createListCardTypeGraph("TG");
		TGraph L_typed = TestsEx2.createTypedPatternGraph(TG, L);
		TGraph G_typed = TestsEx2.createTypedHostGraph(TG, G);
		TPatternMatcher pm_t = new TPatternMatcher(L_typed, G_typed);
		count = 0;
		for (TGraphMorphism m : pm_t.getMatches()) {
			TGraphDiagram d = new TGraphDiagram();
			d.objects(L_typed, G_typed).arrows(m);
			d.prettyPrint(diagrams, "tgraph_match_" + count++);
		}
	}
}
