package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

/**
 * Created by svenv on 09.01.2017.
 */
public class EGraph extends Labelled {

    /**
     * Graph nodes
     */
    public final FinSet Vg;

    /**
     * Attribute nodes
     */
    public final FinSet Vd;

    /**
     * Graph edges
     */
    public final FinSet Eg;

    /**
     * Edges from graph nodes to attributes
     */
    public final FinSet Ena;

    /**
     * Edges from graph edges to attributes
     */
    public final FinSet Eea;

    /**
     * Source node of a graph edge
     */
    public final TotalFunction sourceG;

    /**
     * Target node of a graph edge
     */
    public final TotalFunction targetG;

    /**
     * Source node of a node attribute edge
     */
    public final TotalFunction sourceNA;

    /**
     * Target node of a node attribute edge
     */
    public final TotalFunction targetNA;

    /**
     * Source node of an edge attribute edge
     */
    public final TotalFunction sourceEA;

    /**
     * Target node of an edge attribute edge
     */
    public final TotalFunction targetEA;

    public EGraph(String label,
                  FinSet vg,
                  FinSet vd,
                  FinSet eg,
                  FinSet ena,
                  FinSet eea,
                  TotalFunction sourceG,
                  TotalFunction targetG,
                  TotalFunction sourceNA,
                  TotalFunction targetNA,
                  TotalFunction sourceEA,
                  TotalFunction targetEA) {
        super(label);
        Vg = vg;
        Vd = vd;
        Eg = eg;
        Ena = ena;
        Eea = eea;
        this.sourceG = sourceG;
        this.targetG = targetG;
        this.sourceNA = sourceNA;
        this.targetNA = targetNA;
        this.sourceEA = sourceEA;
        this.targetEA = targetEA;
    }
}
