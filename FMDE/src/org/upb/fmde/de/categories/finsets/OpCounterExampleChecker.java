package org.upb.fmde.de.categories.finsets;

import static org.upb.fmde.de.categories.finsets.FinSets.FinSets;

import org.upb.fmde.de.categories.diagrams.Diagram;

public class OpCounterExampleChecker {
	
	public static boolean isCounterExampleForMono(Diagram<FinSet, TotalFunction> d){
		Diagram<FinSet, TotalFunction> op_d = new Diagram<>(FinSets);
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects()); 
		return CounterExampleChecker.isCounterExampleForEpi(op_d);
	}
	
	public static boolean isCounterExampleForEpi(Diagram<FinSet, TotalFunction> d){
		Diagram<FinSet, TotalFunction> op_d = new Diagram<>(FinSets);
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects());
		return CounterExampleChecker.isCounterExampleForMono(op_d);
	}
}
