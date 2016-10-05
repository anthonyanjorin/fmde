package org.upb.fmde.de.categories.concrete.finsets;

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
		
		Object currentVariable = getNextFreeVariable(index, match);
		for(Object o : host.elts()){
			if (!mono || notInImageOfMatch(match, o)) {
				if (filter.test(currentVariable, o)) {
					TotalFunction match_ = new TotalFunction(match.src(), match.label(), match.trg());
					match_.mappings().putAll(match.mappings());
					match_.addMapping(currentVariable, o);
					mapElements(index + 1, match_);
				}
			}
		}
	}

	private Object getNextFreeVariable(int index, TotalFunction match) {
		Object currentVariable = pattern.elts().get(index);
		if(notYetMatched(match, currentVariable))
			return currentVariable;
		else
			return getNextFreeVariable(index + 1, match);
	}

	private boolean notYetMatched(TotalFunction match, Object currentVariable) {
		return !match.mappings().containsKey(currentVariable);
	}

	private boolean notInImageOfMatch(TotalFunction match, Object o) {
		return !match.mappings().containsValue(o);
	}

	private boolean searchIsComplete(int index) {
		return index == pattern.elts().size();
	}

	private boolean partialMatchIsComplete(TotalFunction match) {
		return match.mappings().keySet().size() == pattern.elts().size();
	}
}
