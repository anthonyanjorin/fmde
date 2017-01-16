package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

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
        ensureValidity();
    }

    private void ensureValidity() {
        String message = "EGraphMorphism " + label + ": " + source.label() + " -> " + target.label() + " is not valid: ";

        Category.ensure(fVg.src().equals(source.Vg), message + fVg.src().label() + " does not match " + source.Vg.label());
        Category.ensure(fVg.trg().equals(target.Vg) , message + fVg.trg().label() + " does not match " + target.Vg.label());

        Category.ensure(fVd.src().equals(source.Vd), message + fVd.src().label() + " does not match " + source.Vd.label());
        Category.ensure(fVd.trg().equals(target.Vd) , message + fVd.trg().label() + " does not match " + target.Vd.label());

        Category.ensure(fEg.src().equals(source.Eg), message + fEg.src().label() + " does not match " + source.Eg.label());
        Category.ensure(fEg.trg().equals(target.Eg) , message + fEg.trg().label() + " does not match " + target.Eg.label());

        Category.ensure(fEna.src().equals(source.Ena), message + fEna.src().label() + " does not match " + source.Ena.label());
        Category.ensure(fEna.trg().equals(target.Ena) , message + fEna.trg().label() + " does not match " + target.Ena.label());

        Category.ensure(fEea.src().equals(source.Eea), message + fEea.src().label() + " does not match " + source.Eea.label());
        Category.ensure(fEea.trg().equals(target.Eea) , message + fEea.trg().label() + " does not match " + target.Eea.label());

        Category.ensure(isStructurePreserving(), message + " It is not structure preserving");
    }

    private boolean isStructurePreserving() {
        return
            FinSets.compose(source.sourceEA, fEg) // pink left
                .isTheSameAs(FinSets.compose(fEea, target.sourceEA)) &&
            FinSets.compose(source.targetEA, fVd) // yellow
                .isTheSameAs(FinSets.compose(fEea, target.targetEA)) &&
            FinSets.compose(source.targetNA, fVd) // cyan
                .isTheSameAs(FinSets.compose(fEna, target.targetNA)) &&
            FinSets.compose(source.sourceNA, fVg) // pink right
                .isTheSameAs(FinSets.compose(fEna, target.sourceNA)) &&
            FinSets.compose(source.sourceG, fVg) // green
                .isTheSameAs(FinSets.compose(fEg, target.sourceG)) &&
            FinSets.compose(source.targetG, fVg) // green
                .isTheSameAs(FinSets.compose(fEg, target.targetG));
    }

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
