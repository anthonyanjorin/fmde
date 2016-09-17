package org.upb.fmde.de.categories;

abstract public class Labelled {
	protected String label = "";
	
	public Labelled(String label) {
		this.label = label;
	}
	
	public String label(){
		return label;
	}
	
	public void label(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
