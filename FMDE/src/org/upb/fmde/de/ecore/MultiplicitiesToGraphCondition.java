package org.upb.fmde.de.ecore;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EReference;
import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphMorphism;
import org.upb.fmde.de.categories.tgraphs.TGraph;
import org.upb.fmde.de.categories.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.tgraphs.TGraphs;
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
	
	public ComplexGraphCondition<TGraph, TGraphMorphism> createGraphConditionFromMultiplicities(){		
		FinSet eRefs = typeGraph.getEdges();
		Collection<ComplexGraphCondition<TGraph, TGraphMorphism>> conditions = eRefs.getElements()
				.stream()
				.map(e ->  multiplicityToCondition((EReference) e))
				.collect(Collectors.toList());

		return new And<>(conditions);
	}

	private ComplexGraphCondition<TGraph, TGraphMorphism> multiplicityToCondition(EReference e) {
		int lower = e.getLowerBound();
		int upper = e.getUpperBound();
		
		switch (lower) {
		case 0:
			return upper == -1? new True<>() : handleUpperBound(e);
		default:
			return upper == -1? handleLowerBound(e) : new And<>(handleLowerBound(e), handleUpperBound(e));
		}
	}

	private ComplexGraphCondition<TGraph, TGraphMorphism> handleLowerBound(EReference e) {
		TGraph P = createAandBs(e, 0);
		FinSet V_P = P.type().getSource().getVertices();
		FinSet E_P = P.type().getSource().getEdges();
		
		TGraph C = createAandBs(e, e.getLowerBound());
		FinSet V_C = C.type().getSource().getVertices();
		FinSet E_C = C.type().getSource().getEdges();
		
		TotalFunction c_V = new TotalFunction(V_P, "c_V", V_C);
		c_V.addMapping(V_P.get("a"), V_C.get("a"));
		
		TotalFunction c_E = new TotalFunction(E_P, "c_E", E_C);
		
		GraphMorphism c_ = new GraphMorphism("c", P.type().getSource(), C.type().getSource(), c_E, c_V);
		TGraphMorphism c = new TGraphMorphism("c", c_, P, C);
		
		return new Constraint<>(TGraphs.TGraphsFor(typeGraph), P, Arrays.asList(c));
	}

	private ComplexGraphCondition<TGraph, TGraphMorphism> handleUpperBound(EReference e) {
		TGraph tN = createAandBs(e, e.getUpperBound() + 1);
		return new NegativeConstraint<>(TGraphs.TGraphsFor(typeGraph), tN);
	}

	private TGraph createAandBs(EReference e, int numberOfBs) {
		FinSet vertices = new FinSet("N_V", "a");
		FinSet edges = new FinSet("E_N");
		
		TotalFunction src = new TotalFunction(edges, "src", vertices);
		TotalFunction trg = new TotalFunction(edges, "trg", vertices);

		edges.getElements().forEach(edge -> src.addMapping(edge, vertices.get("a")));
		for (int i = 0; i < numberOfBs; i++) {
			addB(vertices, edges, src, trg);
		}
				
		Graph N = new Graph("N", edges, vertices, src, trg);
		
		Object A = typeGraph.src().map(e);
		Object B = typeGraph.trg().map(e);
		TotalFunction type_N_V = new TotalFunction(N.getVertices(), "type_N_V", typeGraph.getVertices());
		N.getVertices().getElements().forEach(v -> type_N_V.addMapping(v, v.equals("a")? A : B));
		TotalFunction type_N_E = new TotalFunction(N.getEdges(), "type_N_E", typeGraph.getEdges());		
		N.getEdges().getElements().forEach(edge -> type_N_E.addMapping(edge, e));
		
		GraphMorphism type = new GraphMorphism("type_N", N, typeGraph, type_N_E, type_N_V);
		TGraph tN = new TGraph("N", type);
		return tN;
	}

	private void addB(FinSet vertices, FinSet edges, TotalFunction src, TotalFunction trg) {
		String b = "b";
		String a_b = "a->b";
		vertices.getElements().add(b);
		edges.getElements().add(a_b);
		src.addMapping(a_b, vertices.get("a"));
		trg.addMapping(a_b, b);
	}
}
