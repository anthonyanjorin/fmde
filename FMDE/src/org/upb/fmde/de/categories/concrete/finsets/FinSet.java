package org.upb.fmde.de.categories.concrete.finsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.upb.fmde.de.categories.Labelled;

public class FinSet extends Labelled {
	private List<Object> elements;

	public FinSet(String label, List<Object> elements) {
		super(label);
		this.elements = new ArrayList<Object>(elements);
	}

	public FinSet(String label, Object... elements) {
		this(label, Arrays.asList(elements));
	}

	public List<Object> elts() {
		return elements;
	}

	public Object get(String label) {
		Optional<Object> o = elements.stream().filter(x -> label.equals(x.toString())).findAny();

		return o.orElseThrow(
				() -> new IllegalArgumentException(this.label + " has no element with label \"" + label + "\"."));
	}
}
