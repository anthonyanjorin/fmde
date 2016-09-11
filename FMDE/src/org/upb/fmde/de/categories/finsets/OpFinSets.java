package org.upb.fmde.de.categories.finsets;

import org.upb.fmde.de.categories.OpCategory;

public class OpFinSets extends OpCategory<FinSet, TotalFunction> {

	public static OpFinSets OpFinSets = new OpFinSets(); 
	
	public OpFinSets() {
		super(FinSets.FinSets);
	}
}
