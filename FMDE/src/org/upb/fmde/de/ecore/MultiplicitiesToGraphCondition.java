package org.upb.fmde.de.ecore;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EReference;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
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
		FinSet eRefs = typeGraph.edges();
		Collection<ComplexGraphCondition<TGraph, TGraphMorphism>> conditions = eRefs.elts()
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
		// TODO (21) Implement Chapter 3, Slide 38
	    throw new UnsupportedOperationException("Not implemented yet");
	}

	private ComplexGraphCondition<TGraph, TGraphMorphism> handleUpperBound(EReference e) {
		// TODO (20) Implement Chapter 3, Slide 37
	    throw new UnsupportedOperationException("Not implemented yet");
	}
}
