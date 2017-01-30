package org.upb.fmde.de.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.upb.fmde.de.graphconditions.GraphCondition;

public class TestsApplicationConditions {

	private static final String diagrams = "diagrams/application-conditions/";

	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
		String[] directories = { "application_conditions", "P_C_S_T", "P_R_S", "T_Ti", "Y_Z_X_Di_Zi_Ci" };
		for (String directory : directories) {
			new File(diagrams + directory).mkdir();
			TestUtil.clear(diagrams + directory + "/");
		}
	}

	@Test
	public void applicationConditionsForMoveCard() throws IOException {
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);
		
		// Load L and R
		TGraph[] L_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex5/L.xmi", "L");
		TGraph L = L_TG_Ecore[0];
		
		Graph TG = L_TG_Ecore[1].type().src();
		TGraphs cat = TGraphs.TGraphsFor(TG);
		
		TGraph R = TestUtil.loadBoardAsTGraph(rs, "models/ex5/R.xmi", "R", L_TG_Ecore[1], L_TG_Ecore[2]);
		
		// construct K and l: K -> L, r: K -> R
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
		
		// Construct L_K_R
		Span<TGraphMorphism> L_K_R = new Span<TGraphMorphism>(l, r);
		
		TGraphDiagram d = new TGraphDiagram(TG);
		d.objects(L, K, R).arrows(l, r);
		TestUtil.prettyPrintTEcore(d, "moveCard", diagrams);
		
		// Load P and C
		TGraph P = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/P.xmi", "P", L_TG_Ecore[1], L_TG_Ecore[2]);
		
		TGraph C = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/C1.xmi", "C", L_TG_Ecore[1], L_TG_Ecore[2]);
		
		// Construct graph condition a: P -> C
		pm = new TPatternMatcher(P, C);
		TGraphMorphism a = pm.getMonicMatches().get(0);
		a.label("a");
		
		// calculate application conditions
		List<GraphCondition<TGraph, TGraphMorphism>> applicationConditions = 
				cat.calculateLeftSideApplicationConditions(L_K_R, a);
		
		for (GraphCondition<TGraph, TGraphMorphism> applicationCondition : applicationConditions) {
			d = new TGraphDiagram(TG);

			
			List<TGraph> graphs = new ArrayList<TGraph>();
			List<TGraphMorphism> arrows = new ArrayList<TGraphMorphism>();
			
			TGraphMorphism p = applicationCondition.getP();
			graphs.add(p.src());
			graphs.add(p.trg());
			arrows.add(p);
			
			List<TGraphMorphism> cis = applicationCondition.getCi();
			for (TGraphMorphism ci : cis) {
				TGraph ciTarget = ci.trg();
				ciTarget.type().src().label("Ci"+ciTarget.hashCode());
				graphs.add(ciTarget);
				arrows.add(ci);
			}
			
			d.objects(graphs).arrows(arrows);
			TestUtil.prettyPrintTEcore(d, "application_condition_" + applicationCondition.hashCode(), diagrams + "application_conditions/");
		}
	}
}
