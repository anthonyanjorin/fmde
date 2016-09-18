package org.upb.fmde.de.categories;

public abstract class LabelledArrow<Ob> extends Labelled {

	protected Ob source;
	protected Ob target;
	
	public LabelledArrow(String label, Ob source, Ob target) {
		super(label);
		this.source = source;
		this.target = target;
	}
	
	public Ob src(){
		return source;
	}
	
	public Ob trg(){
		return target;
	}
}
