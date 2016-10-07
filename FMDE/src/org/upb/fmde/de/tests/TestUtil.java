package org.upb.fmde.de.tests;

import java.io.File;
import java.util.Optional;

import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;

public class TestUtil {

	public static void clear(String diagrams) {
		Optional.ofNullable(new File(diagrams)).ifPresent(d -> {
			for (File f : d.listFiles()) 
				f.delete();
		});
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
		TotalFunction f_E = new TotalFunction(pattern.edges(), "f_E", hostGraph.edges())
				.addMapping(pattern.edges().get("cards"), hostGraph.edges().get("e1:cards"));
		TotalFunction f_V = new TotalFunction(pattern.vertices(), "f_V", hostGraph.vertices())
				.addMapping(pattern.vertices().get("L"), hostGraph.vertices().get("j"))
				.addMapping(pattern.vertices().get("C"), hostGraph.vertices().get("x"));
	
		GraphMorphism f = new GraphMorphism("f", pattern, hostGraph, f_E, f_V);
		return f;
	}

	public static TGraph createTypedPatternGraph(Graph TG, Graph G) {
		TotalFunction type_E = new TotalFunction(G.edges(), "type_E", TG.edges())
				.addMapping(G.edges().get("cards"), TG.edges().get("cards"));
	
		TotalFunction type_V = new TotalFunction(G.vertices(), "type_V", TG.vertices())
				.addMapping(G.vertices().get("L"), TG.vertices().get("List"))
				.addMapping(G.vertices().get("C"), TG.vertices().get("Card"));
	
		GraphMorphism type = new GraphMorphism("type", G, TG, type_E, type_V);
		TGraph GT = new TGraph("G", type);
		return GT;
	}

	public static TGraph createTypedHostGraph(Graph TG, Graph G_) {
		TotalFunction type_E_ = new TotalFunction(G_.edges(), "type'_E", TG.edges())
				.addMapping(G_.edges().get("e1:cards"), TG.edges().get("cards"))
				.addMapping(G_.edges().get("e2:cards"), TG.edges().get("cards"))
				.addMapping(G_.edges().get("prev"), TG.edges().get("prev"))
				.addMapping(G_.edges().get("next"), TG.edges().get("next"));

		TotalFunction type_V_ = new TotalFunction(G_.vertices(), "type'_V", TG.vertices())
				.addMapping(G_.vertices().get("i"), TG.vertices().get("List"))
				.addMapping(G_.vertices().get("j"), TG.vertices().get("List"))
				.addMapping(G_.vertices().get("k"), TG.vertices().get("List"))
				.addMapping(G_.vertices().get("x"), TG.vertices().get("Card"))
				.addMapping(G_.vertices().get("y"), TG.vertices().get("Card"));

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