package org.upb.fmde.de.categories.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.finsets.TotalFunction;

public class GraphPatternMatcher extends PatternMatcher<Graph, GraphMorphism> {

	public GraphPatternMatcher(Graph pattern, Graph host) {
		super(pattern, host);
	}

	public List<GraphMorphism> determineMatches(boolean mono){
		return determineMatches(mono, (from, to) -> true, (from, to) -> true);
	}
	
	public List<GraphMorphism> determineMatches(boolean mono, BiPredicate<Object, Object> edgeFilter, BiPredicate<Object, Object> nodeFilter) {
		this.mono = mono;
		List<GraphMorphism> matches = new ArrayList<>();

		FinSetPatternMatcher pm_E = new FinSetPatternMatcher(pattern.getEdges(), host.getEdges());
		for (TotalFunction m_E : pm_E.determineMatches(mono, edgeFilter)) {
			FinSetPatternMatcher pm_V = new FinSetPatternMatcher(pattern.getVertices(), host.getVertices());
			for (TotalFunction m_V : pm_V.determineMatches(mono, determinePartialMatch(m_E), nodeFilter)) {
				try {
					GraphMorphism m = new GraphMorphism("m", pattern, host, m_E, m_V);
					matches.add(m);
				} catch (Exception e) {

				}
			}
		}

		return matches;
	}

	private TotalFunction determinePartialMatch(TotalFunction m_E) {
		TotalFunction m_V = new TotalFunction(pattern.getVertices(), "m_V", host.getVertices());
		m_E.getMappings().forEach((p_e, h_e) -> {
			m_V.addMapping(pattern.src().map(p_e), host.src().map(h_e));
			m_V.addMapping(pattern.trg().map(p_e), host.trg().map(h_e));
		});
		
		return m_V;
	}

}
