package org.upb.fmde.de.tests;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.slicecat.Triangle;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;

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
	}

	@Test
	public void typedGraphExample() throws IOException {
		Graph TG = TestUtil.createListCardTypeGraph("TG");
	
		Graph G = TestUtil.createPatternGraph("G");
		TGraph GT = TestUtil.createTypedPatternGraph(TG, G);
	
		Graph G_ = TestUtil.createHostGraph("G'");
		TGraph GT_ = TestUtil.createTypedHostGraph(TG, G_);
	
		GraphMorphism f = TestUtil.createGraphMorphism(G, G_);
		Triangle<Graph,GraphMorphism> f_typed = new Triangle<Graph,GraphMorphism>("f",GT.type(),f,GT_.type());
	
		TGraphDiagram d_typed = new TGraphDiagram(TG);
		d_typed.arrows(f_typed).objects(GT.type(), GT_.type()).saveAsDot(diagrams, "typed_match_in_TGraphs");
		d_typed.getGraphDiagram().saveAsDot(diagrams, "typed_match_in_Graphs");
		d_typed.getGraphDiagram().getSetDiagram().saveAsDot(diagrams, "typed_match_in_FinSets");		
	}
	
}
