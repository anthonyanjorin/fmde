package org.upb.fmde.de.categories.comma;

import org.upb.fmde.de.categories.LabelledArrow;

public class Triangle<Ob, Arr extends LabelledArrow<Ob>> extends Square<Ob, Arr> {
	
	public Triangle(String label, Arr f, Arr m, Arr g) {
		//Arr id = (Arr("id",f.trg(), g.trg()))f.getClass().newInstance();
		super(label,f,m,g, null);
	}
	
	/**
	 * Checks, if the given Arrows form a valid triangle
	 * 
	 * @return true, if yes
	 */
	public boolean checkValidity(Ob X){
		if (!m.src().equals(f.src()) ||
				!m.trg().equals(g.src()) ||
				!f.trg().equals(g.trg())){
			throw new IllegalArgumentException("Arrows do not form a Triangle!");
		}
	if	(!f.trg().equals(X)){
			throw new IllegalArgumentException(f.label() + " and " + g.label() + " should have the sliced object as their target!");
		}
			else return false;
	}
}
