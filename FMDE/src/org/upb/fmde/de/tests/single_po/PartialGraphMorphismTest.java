package org.upb.fmde.de.tests.single_po;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.pfinsets.PartialFunction;
import org.upb.fmde.de.categories.concrete.pgraphs.PGraphDiagram;
import org.upb.fmde.de.categories.concrete.pgraphs.PGraphMorphism;
import org.upb.fmde.de.tests.TestUtil;

public class PartialGraphMorphismTest {
	private static final String diagrams = "diagrams/single_po/partial_graphmorphism_tests/";

	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void partialGraphMorphismTests() throws IOException {

		Graph pattern = createPatternGraph("Pattern_G");
		
		Graph host = createHostGraph("Host_G");

		PGraphDiagram d_pattern = new PGraphDiagram();
		d_pattern.objects(pattern).saveAsDot(diagrams, "pattern_in_Graphs");
		d_pattern.getSetDiagram().saveAsDot(diagrams, "pattern_in_FinSets");
		
		PGraphDiagram d_graph_ = new PGraphDiagram();
		d_graph_.objects(host).saveAsDot(diagrams, "hostGraph_in_Graphs");
		d_graph_.getSetDiagram().saveAsDot(diagrams, "hostGraph_in_PFinSets");

		PGraphMorphism f = createPGraphMorphism(pattern, host);

		PGraphDiagram d = new PGraphDiagram();
		d.objects(pattern, host).arrows(f);

		d.saveAsDot(diagrams, "match_in_Graphs");
		d.getSetDiagram().saveAsDot(diagrams, "match_in_FinSets");
		d.prettyPrint(diagrams, "match_in_Graphs");

	}
	
	@Test
	public void partialGraphMorphismTests1() throws IOException {

		Graph pattern = createPatternGraph("Pattern_G");
		
		Graph host = createHostGraph("Host_G");

		PGraphMorphism f = createPGraphMorphism1(pattern, host);

		PGraphDiagram d = new PGraphDiagram();
		d.objects(pattern, host).arrows(f);

		d.saveAsDot(diagrams, "match_in_Graphs_Partial");
		d.getSetDiagram().saveAsDot(diagrams, "match_in_FinSets_Partial");
		d.prettyPrint(diagrams, "match_in_Graphs_Partial");

	}
	
	@Test
	public void partialTypedGraphMorphismTests() throws IOException {

		Graph pattern = createPatternGraph("Pattern_G");
		
		Graph host = createHostGraph("Host_G");

		PGraphMorphism f = createPGraphMorphism1(pattern, host);

		PGraphDiagram d = new PGraphDiagram();
		d.objects(pattern, host).arrows(f);

		d.saveAsDot(diagrams, "match_in_Graphs_Partial");
		d.getSetDiagram().saveAsDot(diagrams, "match_in_FinSets_Partial");
		d.prettyPrint(diagrams, "match_in_Graphs_Partial");

	}
	
	public PGraphMorphism createPGraphMorphism(Graph pattern, Graph hostGraph) {
		PartialFunction f_E = new PartialFunction(pattern.edges(), "f_E", hostGraph.edges())
				.addMapping(pattern.edges().get("path"), hostGraph.edges().get("h:path1"));
		PartialFunction f_V = new PartialFunction(pattern.vertices(), "f_V", hostGraph.vertices())
				.addMapping(pattern.vertices().get("field1"), hostGraph.vertices().get("h:field1"))
				.addMapping(pattern.vertices().get("field2"), hostGraph.vertices().get("h:field2"));
	
		PGraphMorphism f = new PGraphMorphism("f", pattern, hostGraph, f_E, f_V);
		return f;
	}
	
	public PGraphMorphism createPGraphMorphism1(Graph pattern, Graph hostGraph) {
		PartialFunction f_E = new PartialFunction(pattern.edges(), "f_E", hostGraph.edges());				
		PartialFunction f_V = new PartialFunction(pattern.vertices(), "f_V", hostGraph.vertices())
				.addMapping(pattern.vertices().get("field1"), hostGraph.vertices().get("h:field1"));
	
		PGraphMorphism f = new PGraphMorphism("f", pattern, hostGraph, f_E, f_V);
		return f;
	}

	public Graph createPatternGraph(String name) {
		FinSet edges = new FinSet("E_" + name, "path");
		FinSet vertices = new FinSet("V_" + name, "field1", "field2");
		TotalFunction source = new TotalFunction(edges, "s_" + name, vertices);
		source.addMapping(edges.get("path"), vertices.get("field1"));		
		TotalFunction target = new TotalFunction(edges, "t_" + name, vertices);
		target.addMapping(edges.get("path"), vertices.get("field2"));		

		return new Graph(name, edges, vertices, source, target);
	}

	public Graph createHostGraph(String name) {
		FinSet edges = new FinSet("E_" + name, "h:path1", "h:path2", "h:path3");
		FinSet vertices = new FinSet("V_" + name, "h:field1", "h:field2", "h:field3", "h:field4");
		TotalFunction source = new TotalFunction(edges, "s_" + name, vertices);
		source.addMapping(edges.get("h:path1"), vertices.get("h:field1"));
		source.addMapping(edges.get("h:path2"), vertices.get("h:field2"));
		source.addMapping(edges.get("h:path3"), vertices.get("h:field3"));
		TotalFunction target = new TotalFunction(edges, "t_" + name, vertices);
		target.addMapping(edges.get("h:path1"), vertices.get("h:field2"));
		target.addMapping(edges.get("h:path2"), vertices.get("h:field4"));
		target.addMapping(edges.get("h:path3"), vertices.get("h:field1"));

		return new Graph(name, edges, vertices, source, target);
	}

}
