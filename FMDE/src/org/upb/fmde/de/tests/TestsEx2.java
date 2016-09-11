package org.upb.fmde.de.tests;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphDiagram;
import org.upb.fmde.de.categories.graphs.GraphMorphism;
import org.upb.fmde.de.categories.tgraphs.TGraph;
import org.upb.fmde.de.categories.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.triplegraphs.TripleGraph;
import org.upb.fmde.de.categories.triplegraphs.TripleGraphDiagram;
import org.upb.fmde.de.categories.triplegraphs.TripleMorphism;
import org.upb.fmde.de.categories.typedtriplegraphs.TTripleGraph;
import org.upb.fmde.de.categories.typedtriplegraphs.TTripleGraphDiagram;
import org.upb.fmde.de.categories.typedtriplegraphs.TTripleMorphism;

public class TestsEx2 {
	private static final String diagrams = "diagrams/ex2/";

	public static void main(String[] args) throws IOException {
		for(File f : new File(diagrams).listFiles()) f.delete();
		
		graphExample();
		typedGraphExample();
		tripleExample();
		tripleMatchExample();
		typedTripleExample();

		System.out.println("All diagrams created.");
	}

	private static void graphExample() throws IOException {
		Graph pattern = createPatternGraph("G");
	
		GraphDiagram d_pattern = new GraphDiagram();
		d_pattern.objects(pattern).saveAsDot(diagrams, "pattern_in_Graphs");
		d_pattern.getSetDiagram().saveAsDot(diagrams, "pattern_in_FinSets");
	
		Graph hostGraph = createHostGraph("G'");
		GraphDiagram d_graph_ = new GraphDiagram();
		d_graph_.objects(hostGraph).saveAsDot(diagrams, "hostGraph_in_Graphs");
		d_graph_.getSetDiagram().saveAsDot(diagrams, "hostGraph_in_FinSets");
	
		GraphMorphism f = createGraphMorphism(pattern, hostGraph);
	
		GraphDiagram d = new GraphDiagram();
		d.objects(pattern, hostGraph).arrows(f);
	
		d.saveAsDot(diagrams, "match_in_Graphs");
		d.getSetDiagram().saveAsDot(diagrams, "match_in_FinSets");
	}

	private static void typedGraphExample() throws IOException {
		Graph TG = createListCardTypeGraph("TG");
	
		Graph G = createPatternGraph("G");
		TGraph GT = createTypedPatternGraph(TG, G);
	
		Graph G_ = createHostGraph("G'");
		TGraph GT_ = createTypedHostGraph(TG, G_);
	
		GraphMorphism f = createGraphMorphism(G, G_);
		TGraphMorphism f_typed = new TGraphMorphism("f", f, GT, GT_);
	
		TGraphDiagram d_typed = new TGraphDiagram();
		d_typed.arrows(f_typed).objects(GT, GT_).saveAsDot(diagrams, "typed_match_in_TGraphs");
		d_typed.getGraphDiagram().saveAsDot(diagrams, "typed_match_in_Graphs");
		d_typed.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "typed_match_in_FinSets");
	}

	private static void tripleExample() throws IOException {
		TripleGraph GT = createPatternTriple("GT");
	
		TripleGraphDiagram d_triples = new TripleGraphDiagram();
		d_triples.objects(GT).saveAsDot(diagrams, "tripleGraph_in_TripleGraphs");
	
		d_triples.getGraphDiagram().saveAsDot(diagrams, "tripleGraph_in_Graphs");
		d_triples.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "tripleGraph_in_FinSets");	
	}

	private static TripleGraph createPatternTriple(String name) {
		Graph G_S = createPatternGraph("G_S");
	
		FinSet E_G_C = new FinSet("E_G_C");
		FinSet V_G_C = new FinSet("V_G_C", "C2T");
		Graph G_C = new Graph("G_C", E_G_C, V_G_C, new TotalFunction(E_G_C, "s_G_C", V_G_C),
				new TotalFunction(E_G_C, "t_G_C", V_G_C));
	
		FinSet E_G_T = new FinSet("E_G_T");
		FinSet V_G_T = new FinSet("V_G_T", "T");
		Graph G_T = new Graph("G_T", E_G_T, V_G_T, new TotalFunction(E_G_T, "s_G_T", V_G_T),
				new TotalFunction(E_G_T, "t_G_T", V_G_T));
	
		TotalFunction sigma_V = new TotalFunction(V_G_C, "sigma_V", G_S.getVertices()).addMapping(V_G_C.get("C2T"),
				G_S.getVertices().get("C"));
		TotalFunction sigma_E = new TotalFunction(E_G_C, "sigma_E", G_S.getEdges());
		GraphMorphism sigma = new GraphMorphism("sigma", G_C, G_S, sigma_E, sigma_V);
	
		TotalFunction tau_V = new TotalFunction(V_G_C, "tau_V", V_G_T).addMapping(V_G_C.get("C2T"), V_G_T.get("T"));
		TotalFunction tau_E = new TotalFunction(E_G_C, "tau_E", E_G_T);
		GraphMorphism tau = new GraphMorphism("tau", G_C, G_T, tau_E, tau_V);
	
		TripleGraph GT = new TripleGraph(name, G_S, sigma, G_C, tau, G_T);
		return GT;
	}

	private static void tripleMatchExample() throws IOException {
		TripleGraph GT = createPatternTriple("GT");	
		TripleGraph GT_ = createHostTriple("GT'");
		TripleMorphism f = createTripleMorphism("f", GT, GT_);
	
		// Create and save diagrams
		TripleGraphDiagram d = new TripleGraphDiagram();
		d.arrows(f).objects(GT_).saveAsDot(diagrams, "tripleMorphism_in_TripleGraphs");
		d.getGraphDiagram().saveAsDot(diagrams, "tripleMorphism_in_Graphs");
		d.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "tripleMorphism_in_FinSets");	
	}

	private static TripleMorphism createTripleMorphism(String name, TripleGraph GT, TripleGraph GT_) {	
		// Create f_T
		TotalFunction f_T_V = new TotalFunction(GT.getG_T().getVertices(), "f_T_V", GT_.getG_T().getVertices())
				.addMapping(GT.getG_T().getVertices().get("T"), GT_.getG_T().getVertices().get("tx"));
		TotalFunction f_T_E = new TotalFunction(GT.getG_T().getEdges(), "f_T_E", GT_.getG_T().getEdges());
		GraphMorphism f_T = new GraphMorphism(name + "_T", GT.getG_T(), GT_.getG_T(), f_T_E, f_T_V);
	
		// Create f_C
		TotalFunction f_C_V = new TotalFunction(GT.getG_C().getVertices(), "f_C_V", GT_.getG_C().getVertices())
				.addMapping(GT.getG_C().getVertices().get("C2T"), GT_.getG_C().getVertices().get("x2tx"));
		TotalFunction f_C_E = new TotalFunction(GT.getG_C().getEdges(), "f_C_E", GT_.getG_C().getEdges());
		GraphMorphism f_C = new GraphMorphism(name + "_C", GT.getG_C(), GT_.getG_C(), f_C_E, f_C_V);
	
		// Create f_S
		TotalFunction f_S_V = new TotalFunction(GT.getG_S().getVertices(), "f_S_V", GT_.getG_S().getVertices())
				.addMapping(GT.getG_S().getVertices().get("L"), GT_.getG_S().getVertices().get("j"))
				.addMapping(GT.getG_S().getVertices().get("C"), GT_.getG_S().getVertices().get("x"));
		TotalFunction f_S_E = new TotalFunction(GT.getG_S().getEdges(), "f_S_E", GT_.getG_S().getEdges())
				.addMapping(GT.getG_S().getEdges().get("cards"), GT_.getG_S().getEdges().get("e1:cards"));
		GraphMorphism f_S = new GraphMorphism(name + "_S", GT.getG_S(), GT_.getG_S(), f_S_E, f_S_V);
	
		return new TripleMorphism(name, GT, GT_, f_S, f_C, f_T);
	}

	private static TripleGraph createHostTriple(String name) {
		Graph G_S_ = createHostGraph(name + "_S");
	
		FinSet E_G_C_ = new FinSet(name + "_E_C");
		FinSet V_G_C_ = new FinSet(name + "_V_C", "x2tx", "y2ty");
		Graph G_C_ = new Graph(name + "_C", E_G_C_, V_G_C_, new TotalFunction(E_G_C_, name + "_s_C", V_G_C_),
				new TotalFunction(E_G_C_, name + "_t_C", V_G_C_));
	
		FinSet E_G_T_ = new FinSet(name + "_E_T", "r");
		FinSet V_G_T_ = new FinSet(name + "V_T", "tx", "ty");
		Graph G_T_ = new Graph(name + "_T", E_G_T_, V_G_T_,
				new TotalFunction(E_G_T_, name + "_s_T", V_G_T_).addMapping(E_G_T_.get("r"), V_G_T_.get("tx")),
				new TotalFunction(E_G_T_, name + "_t_T", V_G_T_).addMapping(E_G_T_.get("r"), V_G_T_.get("ty")));
	
		TotalFunction sigma_V_ = new TotalFunction(V_G_C_, name + "_sigma_V", G_S_.getVertices())
				.addMapping(V_G_C_.get("x2tx"), G_S_.getVertices().get("x"))
				.addMapping(V_G_C_.get("y2ty"), G_S_.getVertices().get("y"));
		TotalFunction sigma_E_ = new TotalFunction(E_G_C_, name + "_sigma_E", G_S_.getEdges());
		GraphMorphism sigma_ = new GraphMorphism(name + "_sigma", G_C_, G_S_, sigma_E_, sigma_V_);
	
		TotalFunction tau_V_ = new TotalFunction(V_G_C_, name + "_tau_V", V_G_T_)
				.addMapping(V_G_C_.get("x2tx"), V_G_T_.get("tx")).addMapping(V_G_C_.get("y2ty"), V_G_T_.get("ty"));
		TotalFunction tau_E_ = new TotalFunction(E_G_C_, name + "_tau_E", E_G_T_);
		GraphMorphism tau_ = new GraphMorphism(name + "_tau", G_C_, G_T_, tau_E_, tau_V_);
	
		TripleGraph GT_ = new TripleGraph(name, G_S_, sigma_, G_C_, tau_, G_T_);
		return GT_;
	}

	private static void typedTripleExample() throws IOException {
		// Create type triple graph
		Graph TG_S = createListCardTypeGraph("TG_S");
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

		TotalFunction sigma_TGT_V = new TotalFunction(TG_C.getVertices(), "sigma_TGT_V", TG_S.getVertices())
				.addMapping(TG_C.getVertices().get("CardToTask"), TG_S.getVertices().get("Card"));
		TotalFunction sigma_TGT_E = new TotalFunction(TG_C.getEdges(), "sigma_TGT_E", TG_S.getEdges());
		GraphMorphism sigma_TGT = new GraphMorphism("sigma_TGT", TG_C, TG_S, sigma_TGT_E, sigma_TGT_V);

		TotalFunction tau_TGT_V = new TotalFunction(TG_C.getVertices(), "tau_TGT_V", TG_T.getVertices())
				.addMapping(TG_C.getVertices().get("CardToTask"), TG_T.getVertices().get("Task"));
		TotalFunction tau_TGT_E = new TotalFunction(TG_C.getEdges(), "tau_TGT_E", TG_T.getEdges());
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
		d.getTripleGraphDiagram().getGraphDiagram().getSetDiagram().saveAsDot(diagrams,
				"typedTripleMorphism_in_FinSets");
	}

	private static TTripleGraph createTypedHostTriple(Graph TG_S, Graph TG_C, Graph TG_T, TripleGraph TGT,
			TripleGraph GT_) {
		TotalFunction type_S_E_ = new TotalFunction(GT_.getG_S().getEdges(), "type'_S_E",
				TG_S.getEdges())
						.addMapping(GT_.getG_S().getEdges().get("e1:cards"),
								TG_S.getEdges().get("cards"))
						.addMapping(GT_.getG_S().getEdges().get("e2:cards"),
								TG_S.getEdges().get("cards"))
						.addMapping(GT_.getG_S().getEdges().get("prev"), TG_S.getEdges().get("prev"))
						.addMapping(GT_.getG_S().getEdges().get("next"), TG_S.getEdges().get("next"));

		TotalFunction type_S_V_ = new TotalFunction(GT_.getG_S().getVertices(), "type'_V",
				TG_S.getVertices())
						.addMapping(GT_.getG_S().getVertices().get("i"),
								TG_S.getVertices().get("List"))
						.addMapping(GT_.getG_S().getVertices().get("j"),
								TG_S.getVertices().get("List"))
						.addMapping(GT_.getG_S().getVertices().get("k"),
								TG_S.getVertices().get("List"))
						.addMapping(GT_.getG_S().getVertices().get("x"),
								TG_S.getVertices().get("Card"))
						.addMapping(GT_.getG_S().getVertices().get("y"),
								TG_S.getVertices().get("Card"));
		GraphMorphism type_S_ = new GraphMorphism("type'_S", GT_.getG_S(), TG_S, type_S_E_, type_S_V_);

		TotalFunction type_C_V_ = new TotalFunction(GT_.getG_C().getVertices(), "type'_C_V",
				TG_C.getVertices())
						.addMapping(GT_.getG_C().getVertices().get("x2tx"),
								TG_C.getVertices().get("CardToTask"))
						.addMapping(GT_.getG_C().getVertices().get("y2ty"),
								TG_C.getVertices().get("CardToTask"));
		TotalFunction type_C_E_ = new TotalFunction(GT_.getG_C().getEdges(), "type_C_E",
				TG_C.getEdges());
		GraphMorphism type_C_ = new GraphMorphism("type'_C", GT_.getG_C(), TG_C, type_C_E_, type_C_V_);

		TotalFunction type_T_V_ = new TotalFunction(GT_.getG_T().getVertices(), "type'_T_V",
				TG_T.getVertices())
						.addMapping(GT_.getG_T().getVertices().get("tx"),
								TG_T.getVertices().get("Task"))
						.addMapping(GT_.getG_T().getVertices().get("ty"),
								TG_T.getVertices().get("Task"));
		TotalFunction type_T_E_ = new TotalFunction(GT_.getG_T().getEdges(), "type'_T_E",
				TG_T.getEdges()).addMapping(GT_.getG_T().getEdges().get("r"),
						TG_T.getEdges().get("related"));
		GraphMorphism type_T_ = new GraphMorphism("type'_T", GT_.getG_T(), TG_T, type_T_E_, type_T_V_);

		TripleMorphism type_ = new TripleMorphism("type'", GT_, TGT, type_S_, type_C_, type_T_);
		TTripleGraph tGT_ = new TTripleGraph("GT'", type_);
		return tGT_;
	}

	private static TTripleGraph createTypedPatternTriple(Graph TG_S, Graph TG_C, Graph TG_T, TripleGraph TGT,
			TripleGraph GT) {
		TotalFunction type_S_V = new TotalFunction(GT.getG_S().getVertices(), "type_S_V",
				TG_S.getVertices())
						.addMapping(GT.getG_S().getVertices().get("L"), TG_S.getVertices().get("List"))
						.addMapping(GT.getG_S().getVertices().get("C"),
								TG_S.getVertices().get("Card"));
		TotalFunction type_S_E = new TotalFunction(GT.getG_S().getEdges(), "type_S_E", TG_S.getEdges())
				.addMapping(GT.getG_S().getEdges().get("cards"), TG_S.getEdges().get("cards"));
		GraphMorphism type_S = new GraphMorphism("type_S", GT.getG_S(), TG_S, type_S_E, type_S_V);

		TotalFunction type_C_V = new TotalFunction(GT.getG_C().getVertices(), "type_C_V",
				TG_C.getVertices()).addMapping(GT.getG_C().getVertices().get("C2T"),
						TG_C.getVertices().get("CardToTask"));
		TotalFunction type_C_E = new TotalFunction(GT.getG_C().getEdges(), "type_C_E",
				TG_C.getEdges());
		GraphMorphism type_C = new GraphMorphism("type_C", GT.getG_C(), TG_C, type_C_E, type_C_V);

		TotalFunction type_T_V = new TotalFunction(GT.getG_T().getVertices(), "type_T_V",
				TG_T.getVertices()).addMapping(GT.getG_T().getVertices().get("T"),
						TG_T.getVertices().get("Task"));
		TotalFunction type_T_E = new TotalFunction(GT.getG_T().getEdges(), "type_T_E",
				TG_T.getEdges());
		GraphMorphism type_T = new GraphMorphism("type_T", GT.getG_T(), TG_T, type_T_E, type_T_V);

		TripleMorphism type = new TripleMorphism("type", GT, TGT, type_S, type_C, type_T);
		TTripleGraph tGT = new TTripleGraph("GT", type);
		return tGT;
	}

	public static Graph createPatternGraph(String name) {
		FinSet edges = new FinSet("E_" + name, "cards");
		FinSet vertices = new FinSet("V_" + name, "L", "C");
		TotalFunction source = new TotalFunction(edges, "s_" + name, vertices);
		source.addMapping(edges.get("cards"), vertices.get("L"));
		TotalFunction target = new TotalFunction(edges, "t_" + name, vertices);
		target.addMapping(edges.get("cards"), vertices.get("C"));
	
		return new Graph(name, edges, vertices, source, target);
	}

	public static Graph createHostGraph(String name) {
		FinSet edges_ = new FinSet("E_" + name, "prev", "next", "e1:cards", "e2:cards");
		FinSet vertices_ = new FinSet("V_" + name, "i", "j", "k", "x", "y");
		TotalFunction source_ = new TotalFunction(edges_, "s_" + name, vertices_);
		source_.addMapping(edges_.get("prev"), vertices_.get("j")).addMapping(edges_.get("next"), vertices_.get("j"))
				.addMapping(edges_.get("e1:cards"), vertices_.get("j"))
				.addMapping(edges_.get("e2:cards"), vertices_.get("k"));
		TotalFunction target_ = new TotalFunction(edges_, "t_" + name, vertices_);
		target_.addMapping(edges_.get("prev"), vertices_.get("i")).addMapping(edges_.get("next"), vertices_.get("k"))
				.addMapping(edges_.get("e1:cards"), vertices_.get("x"))
				.addMapping(edges_.get("e2:cards"), vertices_.get("y"));
	
		return new Graph(name, edges_, vertices_, source_, target_);
	}

	public static GraphMorphism createGraphMorphism(Graph pattern, Graph hostGraph) {
		TotalFunction f_E = new TotalFunction(pattern.getEdges(), "f_E", hostGraph.getEdges())
				.addMapping(pattern.getEdges().get("cards"), hostGraph.getEdges().get("e1:cards"));
		TotalFunction f_V = new TotalFunction(pattern.getVertices(), "f_V", hostGraph.getVertices())
				.addMapping(pattern.getVertices().get("L"), hostGraph.getVertices().get("j"))
				.addMapping(pattern.getVertices().get("C"), hostGraph.getVertices().get("x"));
	
		GraphMorphism f = new GraphMorphism("f", pattern, hostGraph, f_E, f_V);
		return f;
	}

	public static TGraph createTypedPatternGraph(Graph TG, Graph G) {
		TotalFunction type_E = new TotalFunction(G.getEdges(), "type_E", TG.getEdges())
				.addMapping(G.getEdges().get("cards"), TG.getEdges().get("cards"));
	
		TotalFunction type_V = new TotalFunction(G.getVertices(), "type_V", TG.getVertices())
				.addMapping(G.getVertices().get("L"), TG.getVertices().get("List"))
				.addMapping(G.getVertices().get("C"), TG.getVertices().get("Card"));
	
		GraphMorphism type = new GraphMorphism("type", G, TG, type_E, type_V);
		TGraph GT = new TGraph("G", type);
		return GT;
	}

	public static TGraph createTypedHostGraph(Graph TG, Graph G_) {
		TotalFunction type_E_ = new TotalFunction(G_.getEdges(), "type'_E", TG.getEdges())
				.addMapping(G_.getEdges().get("e1:cards"), TG.getEdges().get("cards"))
				.addMapping(G_.getEdges().get("e2:cards"), TG.getEdges().get("cards"))
				.addMapping(G_.getEdges().get("prev"), TG.getEdges().get("prev"))
				.addMapping(G_.getEdges().get("next"), TG.getEdges().get("next"));

		TotalFunction type_V_ = new TotalFunction(G_.getVertices(), "type'_V", TG.getVertices())
				.addMapping(G_.getVertices().get("i"), TG.getVertices().get("List"))
				.addMapping(G_.getVertices().get("j"), TG.getVertices().get("List"))
				.addMapping(G_.getVertices().get("k"), TG.getVertices().get("List"))
				.addMapping(G_.getVertices().get("x"), TG.getVertices().get("Card"))
				.addMapping(G_.getVertices().get("y"), TG.getVertices().get("Card"));

		GraphMorphism type_ = new GraphMorphism("type'", G_, TG, type_E_, type_V_);

		TGraph GT_ = new TGraph("G'", type_);
		return GT_;
	}

	public static Graph createListCardTypeGraph(String name) {
		FinSet E_TG = new FinSet("E_" + name, "prev", "next", "cards");
		FinSet V_TG = new FinSet("V_" + name, "List", "Card");

		TotalFunction s_TG = new TotalFunction(E_TG, "s_" + name, V_TG);
		s_TG.addMapping(E_TG.get("prev"), V_TG.get("List")).addMapping(E_TG.get("next"), V_TG.get("List"))
				.addMapping(E_TG.get("cards"), V_TG.get("List"));

		TotalFunction t_TG = new TotalFunction(E_TG, "t_" + name, V_TG);
		t_TG.addMapping(E_TG.get("prev"), V_TG.get("List")).addMapping(E_TG.get("next"), V_TG.get("List"))
				.addMapping(E_TG.get("cards"), V_TG.get("Card"));

		Graph TG = new Graph(name, E_TG, V_TG, s_TG, t_TG);
		return TG;
	}
}
