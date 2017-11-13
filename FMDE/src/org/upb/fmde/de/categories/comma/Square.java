package org.upb.fmde.de.categories.comma;

import org.upb.fmde.de.categories.LabelledArrow;

public class Square<Ob, Arr extends LabelledArrow<Ob>> extends LabelledArrow<Arr> {
	
	protected Arr m,f,g,n;
	
	public Square(String label, Arr f, Arr m, Arr g, Arr n) {
		super(label,f,g);
		this.m = m;
		this.n = n;
		this.g = g;
		this.f = f;
	}
	
	public Arr getF() {
		return f;
	}
	
	public Arr getM() {
		return m;
	}
	
	public Arr getG() {
		return g;
	}
	
	public Arr getN() {
		return n;
	}
	
	public boolean checkValidity() {
		if (!m.src().equals(f.src()) 
				|| !m.trg().equals(g.src())
				|| !g.trg().equals(n.trg())
				|| !f.trg().equals(n.src())) {
			throw new IllegalArgumentException("Arrows do not form a Square!");
		}
		return true;
	}
}
