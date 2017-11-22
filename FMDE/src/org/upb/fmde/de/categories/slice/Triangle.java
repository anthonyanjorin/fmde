package org.upb.fmde.de.categories.slice;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;

public class Triangle<Ob, Arr extends LabelledArrow<Ob>> extends LabelledArrow<Arr> implements ComparableArrow<Triangle<Ob, Arr>>{
	
	private Arr f;
	private Arr type;
	private Arr type_;
	
	public Triangle(String label, Arr f, Arr type, Arr type_) {
		super(label, type, type_);
		this.f = f;
		this.type = type;
		this.type_ = type_;
	}
	
	/**
	 * Checks, if the given Arrows form a valid triangle
	 * 
	 * @return true, if yes
	 */
	public boolean checkValidity(Ob T){
		if (!f.src().equals(type.src()) ||
				!f.trg().equals(type_.src()) ||
				!type.trg().equals(type_.trg())){
			throw new IllegalArgumentException("Arrows do not form a Triangle!");
		}
	if	(!type.trg().equals(T)){
			throw new IllegalArgumentException(type.label() + " and " + type_.label() + " should have the sliced object as their target!");
		}
			else return false;
	}
	
	public Arr getType() {
		return type;
	}

	public void setType(Arr type) {
		this.type = type;
	}

	public Arr getF() {
		return f;
	}

	public void setM(Arr f) {
		this.f = f;
	}

	public Arr getType_() {
		return type_;
	}

	public void setType_(Arr type_) {
		this.type_ = type_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isTheSameAs(Triangle<Ob, Arr> a) {
		if(f     instanceof ComparableArrow<?> && 
		   type  instanceof ComparableArrow<?> &&
		   type_ instanceof ComparableArrow<?>) {
			ComparableArrow<Arr> fComp = (ComparableArrow<Arr>) f; 
			ComparableArrow<Arr> typeComp = (ComparableArrow<Arr>) type; 
			ComparableArrow<Arr> type_Comp = (ComparableArrow<Arr>) type_; 
			
			return fComp.isTheSameAs(a.f) && typeComp.isTheSameAs(a.type) && type_Comp.isTheSameAs(a.type_);
		} else {
			throw new IllegalStateException("You're trying to compare triangles consisting of uncomparable inner arrows!");
		}
	}
}
