package org.upb.fmde.de.categories.concrete.graphs;

import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

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

		FinSetPatternMatcher pm_E = new FinSetPatternMatcher(pattern.edges(), host.edges());
		for (TotalFunction m_E : pm_E.determineMatches(mono, edgeFilter)) {
			FinSetPatternMatcher pm_V = new FinSetPatternMatcher(pattern.vertices(), host.vertices());
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
		TotalFunction m_V = new TotalFunction(pattern.vertices(), "m_V", host.vertices());
		m_E.mappings().forEach((p_e, h_e) -> {
			m_V.addMapping(pattern.src().map(p_e), host.src().map(h_e));
			m_V.addMapping(pattern.trg().map(p_e), host.trg().map(h_e));
		});
		
		return m_V;
	}

}
