package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.LabelledCategory;

import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

public class EGraphs implements LabelledCategory<EGraph, EGraphMorphism> {

    public static EGraphs eGraphs = new EGraphs();

    private EGraphs() { }

    @Override
    public EGraphMorphism compose(EGraphMorphism f, EGraphMorphism g) {
        return new EGraphMorphism(
            f.label() + ";" + g.label(),
            f.src(),
            f.trg(),
            FinSets.compose(f.fVg, g.fVg),
            FinSets.compose(f.fVd, g.fVd),
            FinSets.compose(f.fVg, g.fVg),
            FinSets.compose(f.fVg, g.fVg),
            FinSets.compose(f.fVg, g.fVg)
        );
    }

    @Override
    public EGraphMorphism id(EGraph g) {
        return new EGraphMorphism("id_" + g.label(), g, g,
            FinSets.id(g.Vg),
            FinSets.id(g.Vd),
            FinSets.id(g.Eg),
            FinSets.id(g.Ena),
            FinSets.id(g.Eea));
    }
}
