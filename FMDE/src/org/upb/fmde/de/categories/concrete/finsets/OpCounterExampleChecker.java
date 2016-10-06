package org.upb.fmde.de.categories.concrete.finsets;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.diagrams.Diagram;

public class OpCounterExampleChecker {
	
	public static boolean isCounterExampleForMono(Diagram<FinSet, TotalFunction> d){
		Diagram<FinSet, TotalFunction> op_d = new Diagram<>(FinSets);
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects()); 
		// TODO (6) Check if d constitutes a counter example for f being a mono
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}
	
	public static boolean isCounterExampleForEpi(Diagram<FinSet, TotalFunction> d){
		Diagram<FinSet, TotalFunction> op_d = new Diagram<>(FinSets);
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects());
		// TODO (7) Check if d constitutes a counter example for f being an epi
		throw new UnsupportedOperationException("This method has not been implemented yet!");
	}
}
