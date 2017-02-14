package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.pgraphs.PGraphs.PGraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.pgraphs.PGraphMorphism;
import org.upb.fmde.de.categories.concrete.pgraphs.PGraphs;

public class TGraphMorphism extends LabelledArrow<TGraph> implements ComparableArrow<TGraphMorphism> {

	private PGraphMorphism f;

	public TGraphMorphism(String label, PGraphMorphism f, TGraph source, TGraph target) {
		super(label, source, target);
		this.f = f;
		if (!isValid())
			throw new IllegalArgumentException("Typed GraphMorphism " + label + ": " + f.src().label() + " -> "
					+ f.trg().label() + " is not valid.");
	}

	private boolean isValid() {
		if (f instanceof GraphMorphism) {
			return PGraphs.compose(f, target.type()).isTheSameAs(source.type());
		} else {
			PGraphMorphism f_ttype = PGraphs.compose(f, target.type());

			if (f_ttype._E().src().elts().stream().allMatch(x -> {
				if (f_ttype._E().map(x) == null) {
					return true;
				}
				return f_ttype._E().map(x).equals(source.type()._E().map(x));
			})) {
				return f_ttype._V().src().elts().stream().allMatch(x -> {
					if (f_ttype._V().map(x) == null) {
						return true;
					}
					return f_ttype._V().map(x).equals(source.type()._V().map(x));
				});
			}
		}
		return false;
	}

	public PGraphMorphism untyped() {
		return f;
	}

	@Override
	public void label(String label) {
		super.label(label);
		f.label(label);
	}

	@Override
	public boolean isTheSameAs(TGraphMorphism a) {
		return source.type().isTheSameAs(a.source.type()) && target.type().isTheSameAs(a.trg().type())
				&& f.isTheSameAs(a.f);
	}
}
