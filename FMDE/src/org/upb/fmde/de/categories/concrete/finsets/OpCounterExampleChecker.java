package org.upb.fmde.de.categories.concrete.finsets;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.OpCategory;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class OpCounterExampleChecker {
	
	public static <O,A extends ComparableArrow<A>> boolean isCounterExampleForMono(Diagram<O, A> d){
		var op_cat = (OpCategory<O,A>)d.getCat();
		Diagram<O, A> op_d = new Diagram<>(op_cat.getUnderlyingCat());
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects()); 
		// TODO (6) Check if d constitutes a counter example for f being a mono
		
		throw new UnsupportedOperationException("This method has not been implemented yet!");	
	}
	
	public static <O,A extends ComparableArrow<A>> boolean isCounterExampleForEpi(Diagram<O, A> d){
		var op_cat = (OpCategory<O,A>)d.getCat();
		Diagram<O, A> op_d = new Diagram<>(op_cat.getUnderlyingCat());		
		op_d.arrows(d.getArrows());
		op_d.objects(d.getObjects());
		// TODO (7) Check if d constitutes a counter example for f being an epi
		
		throw new UnsupportedOperationException("This method has not been implemented yet!");	
	}
}
