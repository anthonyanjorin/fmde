package org.upb.fmde.de.categories.concrete.attgraphs;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

/**
 * Created by svenv on 09.01.2017.
 */
public class AttGraphs implements LabelledCategory<AttGraph, AttGraphMorphism> {

    @Override
    public AttGraphMorphism compose(AttGraphMorphism f, AttGraphMorphism g) {
        return null;
    }

    @Override
    public AttGraphMorphism id(AttGraph o) {
        return null;
    }
}
