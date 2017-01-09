package org.upb.fmde.de.categories.concrete.attgraphs;

import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.egraphs.EGraph;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;

public class AttGraph extends Labelled {

    public final EGraph G;

    // DSIG algebra D = (Sorts, Ops)
    public final FinSet Sorts;
    public final FinSet Ops;

    public AttGraph(String label, EGraph g, FinSet sorts, FinSet ops) {
        super(label);
        G = g;
        Sorts = sorts;
        Ops = ops;
    }
}
