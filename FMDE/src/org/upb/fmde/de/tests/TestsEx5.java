package org.upb.fmde.de.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.colimits.pushouts.DirectDerivation;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
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
		TestUtil.loadSimpleTrello(rs);
		TGraph[] L_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex5/L.xmi", "L");
		TGraph L = L_TG_Ecore[0];
		
		Graph TG = L_TG_Ecore[1].type().src();
		TGraphs cat = TGraphs.TGraphsFor(TG);

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
		
		
		Span<TGraphMorphism> moveCard = new Span<>(cat, l, r);
		
		TGraphDiagram d = new TGraphDiagram(TG);
		d.objects(L, K, R).arrows(l, r);
		TestUtil.prettyPrintTEcore(d, "moveCard", diagrams);		
		
		TGraph G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G", L_TG_Ecore[1], L_TG_Ecore[2]);
		pm = new TPatternMatcher(L, G);
		
		for(TGraphMorphism m : pm.getMonicMatches()){			
			cat.doublePushout(moveCard, m).ifPresent(dd -> {
				TGraphDiagram result = new TGraphDiagram(TG);
				result.objects(L, K, R, G)
					  .arrows(l, r, m)
					  .objects(dd.pushoutComplement.first.trg())
					  .arrows(dd.pushoutComplement.first, dd.pushoutComplement.second)
					  .objects(dd.pushout.horiz.trg())
					  .arrows(dd.pushout.horiz, dd.pushout.vert);
				try {
					TestUtil.prettyPrintTEcore(result, "derivation_" + m.hashCode(), diagrams);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Test
	public void danglingEdge() throws IOException{
		ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
		TestUtil.loadSimpleTrello(rs);
		TGraph[] G_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex3/Board.xmi", "G");
		TGraph G = G_TG_Ecore[0];
		Graph TG = G_TG_Ecore[1].type().src();		
		
		TGraphs cat = TGraphs.TGraphsFor(TG);
		
		FinSet V_L = new FinSet("V_L", "C");
		Graph L_ = new Graph("L", FinSets.FinSets.initialObject().obj, V_L, 
				FinSets.FinSets.initialObject().up.apply(V_L), 
				FinSets.FinSets.initialObject().up.apply(V_L));
		GraphMorphism type_L = new GraphMorphism("type_L", L_, TG, 
				FinSets.FinSets.initialObject().up.apply(TG.edges()), 
				new TotalFunction(L_.vertices(), "type_V", TG.vertices())
						.addMapping(L_.vertices().get("C"), 
								TG.vertices().elts()
									.stream()
									.filter(v -> eMoflonEMFUtil.getName((EObject)v).equals("Card"))
									.findAny()
									.get()));
		TGraph L = new TGraph("L", type_L);
		TGraph K = cat.initialObject().obj;
		TGraph R = cat.initialObject().obj;
		TGraphMorphism l = cat.initialObject().up.apply(L);
		TGraphMorphism r = cat.initialObject().up.apply(R);
		
		Span<TGraphMorphism> deleteCard = new Span<>(cat, l, r);
		
		TGraphDiagram d = new TGraphDiagram(TG);
		d.objects(L, K, R).arrows(l, r);
		TestUtil.prettyPrintTEcore(d, "deleteCard", diagrams);
		
		TPatternMatcher pm = new TPatternMatcher(L, G);
		for(TGraphMorphism m : pm.getMonicMatches()){			
			assertFalse(cat.doublePushout(deleteCard, m).isPresent());
			
			Corner<TGraphMorphism> _m = cat.restrict(new Corner<>(cat, l, m));
			cat.doublePushout(deleteCard, _m.first).ifPresent(dd -> {
				TGraphDiagram result = new TGraphDiagram(TG);
				result.objects(L, K, R, G)
					  .arrows(l, r, m, _m.first, _m.second)
					  .objects(_m.first.trg())
					  .objects(dd.pushoutComplement.first.trg())
					  .arrows(dd.pushoutComplement.first, dd.pushoutComplement.second)
					  .objects(dd.pushout.horiz.trg())
					  .arrows(dd.pushout.horiz, dd.pushout.vert);
				try {
					TestUtil.prettyPrintTEcore(result, "derivation_" + m.hashCode(), diagrams);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}
}
