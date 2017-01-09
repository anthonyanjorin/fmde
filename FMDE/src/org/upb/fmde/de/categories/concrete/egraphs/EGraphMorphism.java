package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

/**
 * Created by svenv on 09.01.2017.
 */
public class EGraphMorphism extends LabelledArrow<EGraph> implements ComparableArrow<EGraphMorphism> {

    public final TotalFunction fVg;
    public final TotalFunction fVd;
    public final TotalFunction fEg;
    public final TotalFunction fEna;
    public final TotalFunction fEea;

    public EGraphMorphism(String label, EGraph source, EGraph target, TotalFunction fVg, TotalFunction fVd, TotalFunction fEg, TotalFunction fEna, TotalFunction fEea) {
        super(label, source, target);
        this.fVg = fVg;
        this.fVd = fVd;
        this.fEg = fEg;
        this.fEna = fEna;
        this.fEea = fEea;
    }

    // TODO: ensureValidity()

    @Override
    public boolean isTheSameAs(EGraphMorphism a) {
        return
            fVg.isTheSameAs(a.fVg) &&
            fVd.isTheSameAs(a.fVd) &&
            fEg.isTheSameAs(a.fEg) &&
            fEna.isTheSameAs(a.fEna) &&
            fEea.isTheSameAs(a.fEea);
    }
}
