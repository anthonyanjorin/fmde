package org.upb.fmde.de.categories.concrete.attgraphs;

import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphMorphism;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

public class AttGraphMorphism extends LabelledArrow<AttGraph> implements ComparableArrow<AttGraphMorphism> {

    public final EGraphMorphism fg;

    public final TotalFunction fsorts;
    public final TotalFunction fops;

    public AttGraphMorphism(String label, AttGraph source, AttGraph target, EGraphMorphism fg, TotalFunction fsorts, TotalFunction fops) {
        super(label, source, target);
        this.fg = fg;
        this.fsorts = fsorts;
        this.fops = fops;
    }

    @Override
    public boolean isTheSameAs(AttGraphMorphism a) {
        return false;
    }
}
