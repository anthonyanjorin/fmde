package org.upb.fmde.de.tests;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher;

public class TestsEx5 {

	private static final String diagrams = "diagrams/ex5/";

	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void doublePushOut() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		EObject root = TestUtil.loadSimpleTrello(rs);
		TGraph[] L_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex5/L.xmi", "L");
		TGraph L = L_TG_Ecore[0];
		Graph TG = L_TG_Ecore[1].type().src();
		TGraph R = TestUtil.loadBoardAsTGraph(rs, "models/ex5/R.xmi", "R", L_TG_Ecore[1], L_TG_Ecore[2]);

		TGraph K = TestUtil.loadBoardAsTGraph(rs, "models/ex5/R.xmi", "K", L_TG_Ecore[1], L_TG_Ecore[2]);
		Object cardsEdge = K.type().src().edges().get("cards");
		Graph graphK = K.type().src();
		K.type()._E().mappings().remove(cardsEdge);
		graphK.src().mappings().remove(cardsEdge);
		graphK.trg().mappings().remove(cardsEdge);
		graphK.edges().elts().remove(cardsEdge);

		TPatternMatcher pm = new TPatternMatcher(K, L);
		TGraphMorphism l = pm.getMonicMatches().get(0);
		l.label("l");
		
		pm = new TPatternMatcher(K, R);
		TGraphMorphism r = pm.getMonicMatches().get(0);
		r.label("r");
		
		Span<TGraphMorphism> moveCard = new Span<>(TGraphs.TGraphsFor(TG), l, r);
		
		
		TGraphDiagram d = new TGraphDiagram(TG);
		d.objects(L, K, R).arrows(l, r);
		TestUtil.prettyPrintTEcore(d, "moveCard", diagrams);		
		
		TGraph G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G", L_TG_Ecore[1], L_TG_Ecore[2]);
		pm = new TPatternMatcher(L, G);
		pm.getMonicMatches().forEach(m -> {			
			Corner<TGraphMorphism> upper_right = new Corner<>(TGraphs.TGraphsFor(TG), l, m);
			// TODO:  Apply rule
		});
		
	}

}
