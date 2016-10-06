package org.upb.fmde.de.categories.concrete.finsets;

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
		// TODO (1) Determine if this and f are equivalent total functions
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}
	
	public Map<Object, Object> mappings(){
		return elementMappings;
	}
	
	public void setMappings(Map<Object, Object> mappings){
		elementMappings.clear();
		elementMappings.putAll(mappings);
	}
}
