package org.upb.fmde.de.categories;

import java.util.ArrayList;
import java.util.List;

public abstract class PatternMatcher<Ob, Arr> {
	protected Ob pattern;
	protected Ob host;
	protected List<Arr> matches;
	protected boolean mono;
	
	protected PatternMatcher(Ob pattern, Ob host) {
		this.pattern = pattern;
		this.host = host;
		matches = new ArrayList<>();
		this.mono = false;
	}
	
	public abstract List<Arr> determineMatches(boolean mono);
	
	public List<Arr> getMatches(){
		return determineMatches(false);
	}
	
	public List<Arr> getMonicMatches() {
		return determineMatches(true);
	}
}
