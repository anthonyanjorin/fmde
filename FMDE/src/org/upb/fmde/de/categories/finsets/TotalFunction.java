package org.upb.fmde.de.categories.finsets;

import java.util.HashMap;
import java.util.Map;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;

public class TotalFunction extends LabelledArrow<FinSet> implements ComparableArrow<TotalFunction> {

	private Map<Object, Object> elementMappings;

	public TotalFunction(FinSet source, String label, FinSet target) {
		super(label, source, target);
		elementMappings = new HashMap<Object, Object>();
	}

	@SuppressWarnings("null") 
	public Object map(Object x) {
		return elementMappings.get(x);
	}

	public TotalFunction addMapping(Object from, Object to) {
		elementMappings.put(from, to);
		return this;
	}

	public boolean isTheSameAs(TotalFunction f) {
		return source.getElements().stream()
					 .allMatch(x -> map(x) != null && map(x).equals(f.map(x)));
	}
	
	public Map<Object, Object> getMappings(){
		return elementMappings;
	}
	
	public void setMappings(Map<Object, Object> mappings){
		elementMappings.clear();
		elementMappings.putAll(mappings);
	}
}
