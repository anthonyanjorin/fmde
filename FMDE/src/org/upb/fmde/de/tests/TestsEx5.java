package org.upb.fmde.de.tests;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher;

public class TestsEx5 {

	private static final String diagrams = "diagrams/ex5/";

	@BeforeClass
	public static void clear() {
		for (File f : new File(diagrams).listFiles())
			f.delete();
	}

	@Test
	public void doublePushOut() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		EObject root = TestsEx3.loadSimpleTrello(rs);
		TGraph[] L_TG_Ecore = TestsEx3.loadBoardAsTGraphs(rs, "models/ex5/L.xmi", "L");
		TGraph L = L_TG_Ecore[0];
		Graph TG = L_TG_Ecore[1].type().src();
		TGraph R = TestsEx3.loadBoardAsTGraph(rs, "models/ex5/R.xmi", "R", L_TG_Ecore[1], L_TG_Ecore[2]);

		TGraph K = TestsEx3.loadBoardAsTGraph(rs, "models/ex5/R.xmi", "K", L_TG_Ecore[1], L_TG_Ecore[2]);
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
		
		// Apply moveCard via DPO
		
		TGraphDiagram d = new TGraphDiagram(TG);
		d.objects(L, K, R).arrows(l, r);
		TestsEx3.prettyPrintTEcore(d, "moveCard", diagrams);		
	}

}
