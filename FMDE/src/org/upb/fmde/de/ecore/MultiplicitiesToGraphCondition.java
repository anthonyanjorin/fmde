package org.upb.fmde.de.ecore;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EReference;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.slice.Triangle;
import org.upb.fmde.de.graphconditions.And;
import org.upb.fmde.de.graphconditions.ComplexGraphCondition;
import org.upb.fmde.de.graphconditions.Constraint;
import org.upb.fmde.de.graphconditions.NegativeConstraint;
import org.upb.fmde.de.graphconditions.True;

public class MultiplicitiesToGraphCondition {
	private Graph typeGraph;
	
	public MultiplicitiesToGraphCondition(Graph typeGraph) {
		this.typeGraph = typeGraph;
	}
	
	public ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> createGraphConditionFromMultiplicities(){		
		FinSet eRefs = typeGraph.edges();
		Collection<ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>>> conditions = eRefs.elts()
				.stream()
				.map(e ->  multiplicityToCondition((EReference) e))
				.collect(Collectors.toList());

		return new And<>(conditions);
	}

	private ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> multiplicityToCondition(EReference e) {
		int lower = e.getLowerBound();
		int upper = e.getUpperBound();
		
		switch (lower) {
		case 0:
			return upper == -1? new True<>() : handleUpperBound(e);
		default:
			return upper == -1? handleLowerBound(e) : new And<>(handleLowerBound(e), handleUpperBound(e));
		}
	}

	private ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> handleLowerBound(EReference e) {
		GraphMorphism P = createAandBs(e, 0);
		FinSet V_P = P.src().vertices();
		FinSet E_P = P.src().edges();
		
		GraphMorphism C = createAandBs(e, e.getLowerBound());
		FinSet V_C = C.src().vertices();
		FinSet E_C = C.src().edges();
		
		TotalFunction c_V = new TotalFunction(V_P, "c_V", V_C);
		c_V.addMapping(V_P.get("a"), V_C.get("a"));
		
		TotalFunction c_E = new TotalFunction(E_P, "c_E", E_C);
		
		GraphMorphism c_ = new GraphMorphism("c", P.src(), C.src(), c_E, c_V);
		Triangle<Graph, GraphMorphism> c = new Triangle<Graph, GraphMorphism>("c", c_, P, C);
		
		return new Constraint(TGraphs.TGraphsFor(typeGraph), P, Arrays.asList(c));
	}

	private ComplexGraphCondition<GraphMorphism, Triangle<Graph, GraphMorphism>> handleUpperBound(EReference e) {
		GraphMorphism tN = createAandBs(e, e.getUpperBound() + 1);
		return new NegativeConstraint(TGraphs.TGraphsFor(typeGraph), tN);
	}

	private GraphMorphism createAandBs(EReference e, int numberOfBs) {
		FinSet vertices = new FinSet("N_V", "a");
		FinSet edges = new FinSet("E_N");
		
		TotalFunction src = new TotalFunction(edges, "src", vertices);
		TotalFunction trg = new TotalFunction(edges, "trg", vertices);

		edges.elts().forEach(edge -> src.addMapping(edge, vertices.get("a")));
		for (int i = 0; i < numberOfBs; i++) {
			addB(vertices, edges, src, trg);
		}
				
		Graph N = new Graph("N", edges, vertices, src, trg);
		
		Object A = typeGraph.src().map(e);
		Object B = typeGraph.trg().map(e);
		TotalFunction type_N_V = new TotalFunction(N.vertices(), "type_N_V", typeGraph.vertices());
		N.vertices().elts().forEach(v -> type_N_V.addMapping(v, v.equals("a")? A : B));
		TotalFunction type_N_E = new TotalFunction(N.edges(), "type_N_E", typeGraph.edges());		
		N.edges().elts().forEach(edge -> type_N_E.addMapping(edge, e));
		
		GraphMorphism type = new GraphMorphism("type_N", N, typeGraph, type_N_E, type_N_V);
		//TGraph tN = new TGraph("N", type);
		return type;
	}

	private void addB(FinSet vertices, FinSet edges, TotalFunction src, TotalFunction trg) {
		String b = "b" + vertices.elts().size();
		String a_b = "a->b" + edges.elts().size();
		vertices.elts().add(b);
		edges.elts().add(a_b);
		src.addMapping(a_b, vertices.get("a"));
		trg.addMapping(a_b, b);
	}
}
