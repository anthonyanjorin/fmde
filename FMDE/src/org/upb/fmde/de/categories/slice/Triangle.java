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
	 * Checks if the arrows f, type and type_ form a valid triangle and if type and type_ are arrows to the slice object T
	 * @param T Slice object
	 */
	public void checkValidity(Ob T){
		//         f
		//   G --------> G'
		//    \         /
		//     \       /
		// type \     /  type'
		//       \   /
		//        v v
		//         T
		
		//TODO Formulate the conditions for the two exceptions to be thrown. The first should deal with the triangle 
		//     structure as depicted above, the second one with the sliced object in particular.
		if (true) {
			throw new IllegalArgumentException("Arrows do not form a Triangle!");
		}
		if (true){
			throw new IllegalArgumentException(type.label() + " and " + type_.label() + " should have the sliced object as their target!");
		}
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
	
	/**
	 * Compares itself with a given triangle
	 * @param a Triangle object to be compared with
	 * @return returns true if both triangles are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isTheSameAs(Triangle<Ob, Arr> a) {
		if(f     instanceof ComparableArrow<?> && 
		   type  instanceof ComparableArrow<?> &&
		   type_ instanceof ComparableArrow<?>) {
			ComparableArrow<Arr> fComp = (ComparableArrow<Arr>) f; 
			ComparableArrow<Arr> typeComp = (ComparableArrow<Arr>) type; 
			ComparableArrow<Arr> type_Comp = (ComparableArrow<Arr>) type_; 
			
			//TODO When are two triangle objects equal? Reuse the comparable arrows created here and think about how
			//     these arrows can be compared.
			return false;
		} else {
			throw new IllegalStateException("You're trying to compare triangles consisting of uncomparable inner arrows!");
		}
	}
}
