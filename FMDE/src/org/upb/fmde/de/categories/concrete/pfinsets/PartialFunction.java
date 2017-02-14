package org.upb.fmde.de.categories.concrete.pfinsets;

import java.util.HashMap;
import java.util.Map;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

public class PartialFunction extends LabelledArrow<FinSet> implements ComparableArrow<PartialFunction> {

	private Map<Object, Object> elementMappings;
	
	public PartialFunction(FinSet source, String label, FinSet target) {
		super(label, source, target);
		elementMappings = new HashMap<Object, Object>();
	}


	public Object map(Object x) {
		return elementMappings.get(x);
	}

	public PartialFunction addMapping(Object from, Object to) {
		elementMappings.put(from, to);
		return this;
	}

	public boolean isTheSameAs(PartialFunction f) {
		return source.elts().stream()
					 .allMatch(x -> {
						 if (map(x) == null) {
							 return f.map(x) == null;
						 }
						 return map(x).equals(f.map(x));
					 });
	}
	
	public Map<Object, Object> mappings(){
		return elementMappings;
	}
	
	public void setMappings(Map<Object, Object> mappings){
		elementMappings.clear();
		elementMappings.putAll(mappings);
	}
}
