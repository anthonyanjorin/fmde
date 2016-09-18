package org.upb.fmde.de.categories.concrete.typedtriplegraphs;

import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.triplegraphs.TripleMorphism;

public class TTripleGraph extends Labelled {

	private TripleMorphism type;
	
	public TTripleGraph(String label, TripleMorphism type) {
		super(label);
		this.type = type;
	}
	
	public TripleMorphism getTypeMorphism(){
		return type;
	}
}
