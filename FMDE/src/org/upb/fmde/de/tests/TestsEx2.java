package org.upb.fmde.de.tests;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleGraphDiagram;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.concrete.typedtriplegraphs.TTripleGraph;
import org.upb.fmde.de.categories.concrete.typedtriplegraphs.TTripleGraphDiagram;
import org.upb.fmde.de.categories.concrete.typedtriplegraphs.TTripleMorphism;

public class TestsEx2 {
	private static final String diagrams = "diagrams/ex2/";

	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void graphExample() throws IOException {
		Graph pattern = TestUtil.createPatternGraph("G");
	
		GraphDiagram d_pattern = new GraphDiagram();
		d_pattern.objects(pattern).saveAsDot(diagrams, "pattern_in_Graphs");
		d_pattern.getSetDiagram().saveAsDot(diagrams, "pattern_in_FinSets");
	
		Graph hostGraph = TestUtil.createHostGraph("G'");
		GraphDiagram d_graph_ = new GraphDiagram();
		d_graph_.objects(hostGraph).saveAsDot(diagrams, "hostGraph_in_Graphs");
		d_graph_.getSetDiagram().saveAsDot(diagrams, "hostGraph_in_FinSets");
	
		GraphMorphism f = TestUtil.createGraphMorphism(pattern, hostGraph);
	
		GraphDiagram d = new GraphDiagram();
		d.objects(pattern, hostGraph).arrows(f);
	
		d.saveAsDot(diagrams, "match_in_Graphs");
		d.getSetDiagram().saveAsDot(diagrams, "match_in_FinSets");
		d.prettyPrint(diagrams, "match_in_Graphs");
		
		//TODO Add some assertions here
	}

	@Test
	public void typedGraphExample() throws IOException {
		Graph TG = TestUtil.createListCardTypeGraph("TG");
	
		Graph G = TestUtil.createPatternGraph("G");
		TGraph GT = TestUtil.createTypedPatternGraph(TG, G);
	
		Graph G_ = TestUtil.createHostGraph("G'");
		TGraph GT_ = TestUtil.createTypedHostGraph(TG, G_);
	
		GraphMorphism f = TestUtil.createGraphMorphism(G, G_);
		TGraphMorphism f_typed = new TGraphMorphism("f", f, GT, GT_);
	
		TGraphDiagram d_typed = new TGraphDiagram(TG);
		d_typed.arrows(f_typed).objects(GT, GT_).saveAsDot(diagrams, "typed_match_in_TGraphs");
		d_typed.getGraphDiagram().saveAsDot(diagrams, "typed_match_in_Graphs");
		d_typed.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "typed_match_in_FinSets");
		
		//TODO Add some assertions here
	}
	
	@Test
	public void tripleExample() throws IOException {
		TripleGraph GT = createPatternTriple("GT");
	
		TripleGraphDiagram d_triples = new TripleGraphDiagram();
		d_triples.objects(GT).saveAsDot(diagrams, "tripleGraph_in_TripleGraphs");
	
		d_triples.getGraphDiagram().saveAsDot(diagrams, "tripleGraph_in_Graphs");
		d_triples.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "tripleGraph_in_FinSets");	
		
		//TODO Add some assertions here
	}

	@Test
	public void tripleMatchExample() throws IOException {
		TripleGraph GT = createPatternTriple("GT");	
		TripleGraph GT_ = createHostTriple("GT'");
		TripleMorphism f = createTripleMorphism("f", GT, GT_);
	
		// Create and save diagrams
		TripleGraphDiagram d = new TripleGraphDiagram();
		d.arrows(f).objects(GT_).saveAsDot(diagrams, "tripleMorphism_in_TripleGraphs");
		d.getGraphDiagram().saveAsDot(diagrams, "tripleMorphism_in_Graphs");
		d.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "tripleMorphism_in_FinSets");	
		
		//TODO Add some assertions here
	}

	@Test
	public void typedTripleExample() throws IOException {
		// Create type triple graph
		Graph TG_S = TestUtil.createListCardTypeGraph("TG_S");
		FinSet TG_C_E = new FinSet("TG_C_E");
		FinSet TG_C_V = new FinSet("TG_C_V", "CardToTask");
		TotalFunction s_TG_C = new TotalFunction(TG_C_E, "s_TG_C", TG_C_V);
		TotalFunction t_TG_C = new TotalFunction(TG_C_E, "t_TG_C", TG_C_V);
		Graph TG_C = new Graph("TG_C", TG_C_E, TG_C_V, s_TG_C, t_TG_C);
	
		FinSet TG_T_E = new FinSet("TG_T_E", "related");
		FinSet TG_T_V = new FinSet("TG_T_V", "Task");
		TotalFunction s_TG_T = new TotalFunction(TG_T_E, "s_TG_T", TG_T_V).addMapping(TG_T_E.get("related"),
				TG_T_V.get("Task"));
		TotalFunction t_TG_T = new TotalFunction(TG_T_E, "t_TG_T", TG_T_V).addMapping(TG_T_E.get("related"),
				TG_T_V.get("Task"));
		Graph TG_T = new Graph("TG_T", TG_T_E, TG_T_V, s_TG_T, t_TG_T);
	
		TotalFunction sigma_TGT_V = new TotalFunction(TG_C.vertices(), "sigma_TGT_V", TG_S.vertices())
				.addMapping(TG_C.vertices().get("CardToTask"), TG_S.vertices().get("Card"));
		TotalFunction sigma_TGT_E = new TotalFunction(TG_C.edges(), "sigma_TGT_E", TG_S.edges());
		GraphMorphism sigma_TGT = new GraphMorphism("sigma_TGT", TG_C, TG_S, sigma_TGT_E, sigma_TGT_V);
	
		TotalFunction tau_TGT_V = new TotalFunction(TG_C.vertices(), "tau_TGT_V", TG_T.vertices())
				.addMapping(TG_C.vertices().get("CardToTask"), TG_T.vertices().get("Task"));
		TotalFunction tau_TGT_E = new TotalFunction(TG_C.edges(), "tau_TGT_E", TG_T.edges());
		GraphMorphism tau_TGT = new GraphMorphism("tau_TGT", TG_C, TG_T, tau_TGT_E, tau_TGT_V);
	
		TripleGraph TGT = new TripleGraph("TGT", TG_S, sigma_TGT, TG_C, tau_TGT, TG_T);
	
		// Create typed triple graphs
		TripleGraph GT = createPatternTriple("GT");
		TTripleGraph tGT = createTypedPatternTriple(TG_S, TG_C, TG_T, TGT, GT);
	
		TripleGraph GT_ = createHostTriple("GT'");
		TTripleGraph tGT_ = createTypedHostTriple(TG_S, TG_C, TG_T, TGT, GT_);
	
		// Created type triple morphism
		TripleMorphism f = createTripleMorphism("f", GT, GT_);
		TTripleMorphism tf = new TTripleMorphism("f", f, tGT, tGT_);
	
		// Create and save diagrams
		TTripleGraphDiagram d = new TTripleGraphDiagram();
		d.objects(tGT, tGT_).arrows(tf).saveAsDot(diagrams, "typedTripleMorphism_in_TypedTripleGraphs");
		d.getTripleGraphDiagram().saveAsDot(diagrams, "typedTripleMorphism_in_TripleGraphs");
		d.getTripleGraphDiagram().getGraphDiagram().saveAsDot(diagrams, "typedTripleMorphism_in_Graphs");
		d.getTripleGraphDiagram().getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "typedTripleMorphism_in_FinSets");
		
		//TODO Add some assertions here
	}

	private TripleGraph createPatternTriple(String name) {
		Graph G_S = TestUtil.createPatternGraph("G_S");
	
		FinSet E_G_C = new FinSet("E_G_C");
		FinSet V_G_C = new FinSet("V_G_C", "C2T");
		Graph G_C = new Graph("G_C", E_G_C, V_G_C, new TotalFunction(E_G_C, "s_G_C", V_G_C),
				new TotalFunction(E_G_C, "t_G_C", V_G_C));
	
		FinSet E_G_T = new FinSet("E_G_T");
		FinSet V_G_T = new FinSet("V_G_T", "T");
		Graph G_T = new Graph("G_T", E_G_T, V_G_T, new TotalFunction(E_G_T, "s_G_T", V_G_T),
				new TotalFunction(E_G_T, "t_G_T", V_G_T));
	
		TotalFunction sigma_V = new TotalFunction(V_G_C, "sigma_V", G_S.vertices()).addMapping(V_G_C.get("C2T"),
				G_S.vertices().get("C"));
		TotalFunction sigma_E = new TotalFunction(E_G_C, "sigma_E", G_S.edges());
		GraphMorphism sigma = new GraphMorphism("sigma", G_C, G_S, sigma_E, sigma_V);
	
		TotalFunction tau_V = new TotalFunction(V_G_C, "tau_V", V_G_T).addMapping(V_G_C.get("C2T"), V_G_T.get("T"));
		TotalFunction tau_E = new TotalFunction(E_G_C, "tau_E", E_G_T);
		GraphMorphism tau = new GraphMorphism("tau", G_C, G_T, tau_E, tau_V);
	
		TripleGraph GT = new TripleGraph(name, G_S, sigma, G_C, tau, G_T);
		return GT;
	}

	private TripleMorphism createTripleMorphism(String name, TripleGraph GT, TripleGraph GT_) {	
		// Create f_T
		TotalFunction f_T_V = new TotalFunction(GT.getG_T().vertices(), "f_T_V", GT_.getG_T().vertices())
				.addMapping(GT.getG_T().vertices().get("T"), GT_.getG_T().vertices().get("tx"));
		TotalFunction f_T_E = new TotalFunction(GT.getG_T().edges(), "f_T_E", GT_.getG_T().edges());
		GraphMorphism f_T = new GraphMorphism(name + "_T", GT.getG_T(), GT_.getG_T(), f_T_E, f_T_V);
	
		// Create f_C
		TotalFunction f_C_V = new TotalFunction(GT.getG_C().vertices(), "f_C_V", GT_.getG_C().vertices())
				.addMapping(GT.getG_C().vertices().get("C2T"), GT_.getG_C().vertices().get("x2tx"));
		TotalFunction f_C_E = new TotalFunction(GT.getG_C().edges(), "f_C_E", GT_.getG_C().edges());
		GraphMorphism f_C = new GraphMorphism(name + "_C", GT.getG_C(), GT_.getG_C(), f_C_E, f_C_V);
	
		// Create f_S
		TotalFunction f_S_V = new TotalFunction(GT.getG_S().vertices(), "f_S_V", GT_.getG_S().vertices())
				.addMapping(GT.getG_S().vertices().get("L"), GT_.getG_S().vertices().get("j"))
				.addMapping(GT.getG_S().vertices().get("C"), GT_.getG_S().vertices().get("x"));
		TotalFunction f_S_E = new TotalFunction(GT.getG_S().edges(), "f_S_E", GT_.getG_S().edges())
				.addMapping(GT.getG_S().edges().get("cards"), GT_.getG_S().edges().get("e1:cards"));
		GraphMorphism f_S = new GraphMorphism(name + "_S", GT.getG_S(), GT_.getG_S(), f_S_E, f_S_V);
	
		return new TripleMorphism(name, GT, GT_, f_S, f_C, f_T);
	}

	private TripleGraph createHostTriple(String name) {
		Graph G_S_ = TestUtil.createHostGraph(name + "_S");
	
		FinSet E_G_C_ = new FinSet(name + "_E_C");
		FinSet V_G_C_ = new FinSet(name + "_V_C", "x2tx", "y2ty");
		Graph G_C_ = new Graph(name + "_C", E_G_C_, V_G_C_, new TotalFunction(E_G_C_, name + "_s_C", V_G_C_),
				new TotalFunction(E_G_C_, name + "_t_C", V_G_C_));
	
		FinSet E_G_T_ = new FinSet(name + "_E_T", "r");
		FinSet V_G_T_ = new FinSet(name + "V_T", "tx", "ty");
		Graph G_T_ = new Graph(name + "_T", E_G_T_, V_G_T_,
				new TotalFunction(E_G_T_, name + "_s_T", V_G_T_).addMapping(E_G_T_.get("r"), V_G_T_.get("tx")),
				new TotalFunction(E_G_T_, name + "_t_T", V_G_T_).addMapping(E_G_T_.get("r"), V_G_T_.get("ty")));
	
		TotalFunction sigma_V_ = new TotalFunction(V_G_C_, name + "_sigma_V", G_S_.vertices())
				.addMapping(V_G_C_.get("x2tx"), G_S_.vertices().get("x"))
				.addMapping(V_G_C_.get("y2ty"), G_S_.vertices().get("y"));
		TotalFunction sigma_E_ = new TotalFunction(E_G_C_, name + "_sigma_E", G_S_.edges());
		GraphMorphism sigma_ = new GraphMorphism(name + "_sigma", G_C_, G_S_, sigma_E_, sigma_V_);
	
		TotalFunction tau_V_ = new TotalFunction(V_G_C_, name + "_tau_V", V_G_T_)
				.addMapping(V_G_C_.get("x2tx"), V_G_T_.get("tx")).addMapping(V_G_C_.get("y2ty"), V_G_T_.get("ty"));
		TotalFunction tau_E_ = new TotalFunction(E_G_C_, name + "_tau_E", E_G_T_);
		GraphMorphism tau_ = new GraphMorphism(name + "_tau", G_C_, G_T_, tau_E_, tau_V_);
	
		TripleGraph GT_ = new TripleGraph(name, G_S_, sigma_, G_C_, tau_, G_T_);
		return GT_;
	}

	private TTripleGraph createTypedHostTriple(Graph TG_S, Graph TG_C, Graph TG_T, TripleGraph TGT, TripleGraph GT_) {
		TotalFunction type_S_E_ = new TotalFunction(GT_.getG_S().edges(), "type'_S_E", TG_S.edges())
						.addMapping(GT_.getG_S().edges().get("e1:cards"),
								TG_S.edges().get("cards"))
						.addMapping(GT_.getG_S().edges().get("e2:cards"),
								TG_S.edges().get("cards"))
						.addMapping(GT_.getG_S().edges().get("prev"), TG_S.edges().get("prev"))
						.addMapping(GT_.getG_S().edges().get("next"), TG_S.edges().get("next"));

		TotalFunction type_S_V_ = new TotalFunction(GT_.getG_S().vertices(), "type'_V", TG_S.vertices())
						.addMapping(GT_.getG_S().vertices().get("i"),
								TG_S.vertices().get("List"))
						.addMapping(GT_.getG_S().vertices().get("j"),
								TG_S.vertices().get("List"))
						.addMapping(GT_.getG_S().vertices().get("k"),
								TG_S.vertices().get("List"))
						.addMapping(GT_.getG_S().vertices().get("x"),
								TG_S.vertices().get("Card"))
						.addMapping(GT_.getG_S().vertices().get("y"),
								TG_S.vertices().get("Card"));
		GraphMorphism type_S_ = new GraphMorphism("type'_S", GT_.getG_S(), TG_S, type_S_E_, type_S_V_);

		TotalFunction type_C_V_ = new TotalFunction(GT_.getG_C().vertices(), "type'_C_V", TG_C.vertices())
						.addMapping(GT_.getG_C().vertices().get("x2tx"),
								TG_C.vertices().get("CardToTask"))
						.addMapping(GT_.getG_C().vertices().get("y2ty"),
								TG_C.vertices().get("CardToTask"));
		TotalFunction type_C_E_ = new TotalFunction(GT_.getG_C().edges(), "type_C_E", TG_C.edges());
		GraphMorphism type_C_ = new GraphMorphism("type'_C", GT_.getG_C(), TG_C, type_C_E_, type_C_V_);

		TotalFunction type_T_V_ = new TotalFunction(GT_.getG_T().vertices(), "type'_T_V", TG_T.vertices())
						.addMapping(GT_.getG_T().vertices().get("tx"), TG_T.vertices().get("Task"))
						.addMapping(GT_.getG_T().vertices().get("ty"), TG_T.vertices().get("Task"));
		TotalFunction type_T_E_ = new TotalFunction(GT_.getG_T().edges(), "type'_T_E", TG_T.edges())
				.addMapping(GT_.getG_T().edges().get("r"), TG_T.edges().get("related"));
		GraphMorphism type_T_ = new GraphMorphism("type'_T", GT_.getG_T(), TG_T, type_T_E_, type_T_V_);

		TripleMorphism type_ = new TripleMorphism("type'", GT_, TGT, type_S_, type_C_, type_T_);
		TTripleGraph tGT_ = new TTripleGraph("GT'", type_);
		return tGT_;
	}

	private TTripleGraph createTypedPatternTriple(Graph TG_S, Graph TG_C, Graph TG_T, TripleGraph TGT, TripleGraph GT) {
		TotalFunction type_S_V = new TotalFunction(GT.getG_S().vertices(), "type_S_V",TG_S.vertices())
				.addMapping(GT.getG_S().vertices().get("L"), TG_S.vertices().get("List"))
				.addMapping(GT.getG_S().vertices().get("C"), TG_S.vertices().get("Card"));
		TotalFunction type_S_E = new TotalFunction(GT.getG_S().edges(), "type_S_E", TG_S.edges())
				.addMapping(GT.getG_S().edges().get("cards"), TG_S.edges().get("cards"));
		GraphMorphism type_S = new GraphMorphism("type_S", GT.getG_S(), TG_S, type_S_E, type_S_V);

		TotalFunction type_C_V = new TotalFunction(GT.getG_C().vertices(), "type_C_V", TG_C.vertices())
				.addMapping(GT.getG_C().vertices().get("C2T"), TG_C.vertices().get("CardToTask"));
		TotalFunction type_C_E = new TotalFunction(GT.getG_C().edges(), "type_C_E", TG_C.edges());
		GraphMorphism type_C = new GraphMorphism("type_C", GT.getG_C(), TG_C, type_C_E, type_C_V);

		TotalFunction type_T_V = new TotalFunction(GT.getG_T().vertices(), "type_T_V", TG_T.vertices())
				.addMapping(GT.getG_T().vertices().get("T"), TG_T.vertices().get("Task"));
		TotalFunction type_T_E = new TotalFunction(GT.getG_T().edges(), "type_T_E", TG_T.edges());
		GraphMorphism type_T = new GraphMorphism("type_T", GT.getG_T(), TG_T, type_T_E, type_T_V);

		TripleMorphism type = new TripleMorphism("type", GT, TGT, type_S, type_C, type_T);
		TTripleGraph tGT = new TTripleGraph("GT", type);
		return tGT;
	}
}
