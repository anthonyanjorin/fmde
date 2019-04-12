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

	public Object map(Object x) {
		return elementMappings.get(x);
	}

	public TotalFunction addMapping(Object from, Object to) {
		elementMappings.put(from, to);
		return this;
	}

	public boolean isTheSameAs(TotalFunction f) {
		// Check domain and co-domain
		return (source.equals(f.source) && target.equals(f.target)) &&
		// Mappings should be the same
				elementMappings.equals(f.elementMappings);
	}

	public Map<Object, Object> mappings() {
		return elementMappings;
	}

	public void setMappings(Map<Object, Object> mappings) {
		elementMappings.clear();
		elementMappings.putAll(mappings);
	}
}
