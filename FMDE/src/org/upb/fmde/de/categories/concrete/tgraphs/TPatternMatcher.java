package org.upb.fmde.de.categories.concrete.tgraphs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.GraphPatternMatcher;
import org.upb.fmde.de.categories.slice.Triangle;

public class TPatternMatcher extends PatternMatcher<GraphMorphism, Triangle<Graph, GraphMorphism>>{
	
	public TPatternMatcher(GraphMorphism pattern, GraphMorphism host) {
		super(pattern, host);
	}

	public List<Triangle<Graph, GraphMorphism>> determineMatches(boolean mono) {
		List<Triangle<Graph, GraphMorphism>> typedMatches = new ArrayList<>();
		
		GraphPatternMatcher pm = new GraphPatternMatcher(pattern.src(), host.src());
		for (GraphMorphism m : pm.determineMatches(mono, createEdgeTypeFilter(), createNodeTypeFilter())) {
			try {
				Triangle<Graph, GraphMorphism> typedMatch = new Triangle<Graph, GraphMorphism>("m", m, pattern, host);
				typedMatches.add(typedMatch);
			} catch (Exception e) {
				
			}
		}
		
		return typedMatches;
	}

	private BiPredicate<Object, Object> createNodeTypeFilter() {
		return (from, to) -> {
			Object t_from = pattern._V().map(from);
			Object t_to = host._V().map(to);
			return t_from.equals(t_to);
		};
	}

	private BiPredicate<Object, Object> createEdgeTypeFilter() {
		return (from, to) -> {
			Object t_from = pattern._E().map(from);
			Object t_to = host._E().map(to);
			return t_from.equals(t_to);
		};
	}
}
