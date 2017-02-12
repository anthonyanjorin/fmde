package org.upb.fmde.de.categories.concrete.slicecat;

import org.upb.fmde.de.categories.LabelledArrow;

public class Triangle<Ob, Arr extends LabelledArrow<Ob>> extends LabelledArrow<Arr>{
	
	private Arr m, f, g;
	
	public Triangle(String label, Arr f, Arr m, Arr g){
		super(label,f,g);
		this.m = m;
		this.f = f;
		this.g = g;
	}
	
	public Arr getF(){
		return f;
	}

	public Arr getM() {
		return m;
	}

	public Arr getG() {
		return g;
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
