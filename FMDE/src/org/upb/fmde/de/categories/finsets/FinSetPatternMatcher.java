package org.upb.fmde.de.categories.finsets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.upb.fmde.de.categories.PatternMatcher;

public class FinSetPatternMatcher extends PatternMatcher<FinSet, TotalFunction> {
	
	BiPredicate<Object, Object> filter = (from, to) -> true;
	
	public FinSetPatternMatcher(FinSet pattern, FinSet host) {
		super(pattern, host);
	}
	
	public List<TotalFunction> determineMatches(boolean mono, TotalFunction partialMatch, BiPredicate<Object, Object> filter){
		this.mono = mono;
		this.filter = filter;
		matches = new ArrayList<>();
		mapElements(0, partialMatch);
		return matches;
	}

	public List<TotalFunction> determineMatches(boolean mono){
		return determineMatches(mono, new TotalFunction(pattern, "m", host), filter);
	}
	
	public List<TotalFunction> determineMatches(boolean mono, BiPredicate<Object, Object> filter){
		return determineMatches(mono, new TotalFunction(pattern, "m", host), filter);
	}
	
	private void mapElements(int index, TotalFunction match) {
		if(searchIsComplete(index) || partialMatchIsComplete(match)){
			matches.add(match);
			return;
		}
		
		Object currentVariable = pattern.getElements().get(index);
		for(Object o : host.getElements()){
			if (!mono || notInImageOfMatch(match, o)) {
				if (notYetMatched(match, currentVariable)) {
					if (filter.test(currentVariable, o)) {
						TotalFunction match_ = new TotalFunction(match.getSource(), match.label(), match.getTarget());
						match_.getMappings().putAll(match.getMappings());
						match_.addMapping(currentVariable, o);
						mapElements(index + 1, match_);
					}
				}
			}
		}
	}

	private boolean notYetMatched(TotalFunction match, Object currentVariable) {
		return !match.getMappings().containsKey(currentVariable);
	}

	private boolean notInImageOfMatch(TotalFunction match, Object o) {
		return !match.getMappings().containsValue(o);
	}

	private boolean searchIsComplete(int index) {
		return index == pattern.getElements().size();
	}

	private boolean partialMatchIsComplete(TotalFunction match) {
		return match.getMappings().keySet().size() == pattern.getElements().size();
	}
}
