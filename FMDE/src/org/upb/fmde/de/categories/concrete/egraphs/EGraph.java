package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.Labelled;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import java.util.HashSet;

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
        ensureValidity();
    }

    private void ensureValidity() {
        String s = "EGraph " + label + " is invalid: ";

        // Check graph nodes and edges
        Category.ensure(sourceG.src().equals(Eg), s + "sourceG function doesn't fit to set of graph edges");
        Category.ensure(sourceG.trg().equals(Vg), s + "sourceG function doesn't fit to set of graph vertices");
        Category.ensure(targetG.src().equals(Eg), s + "targetG function doesn't fit to set of graph edges");
        Category.ensure(targetG.trg().equals(Vg), s + "targetG function doesn't fit to set of graph vertices");

        // Check data nodes and node/edge attribute edges
        Category.ensure(sourceNA.src().equals(Ena), s + "sourceNA function doesn't fit to set of node attribute edges");
        Category.ensure(sourceNA.trg().equals(Vg), s + "sourceNA function doesn't fit to set of graph nodes");
        Category.ensure(targetNA.src().equals(Ena), s + "targetNA function doesn't fit to set of node attribute edges");
        Category.ensure(targetNA.trg().equals(Vd), s + "targetNA function doesn't fit to set of data nodes");
        Category.ensure(sourceEA.src().equals(Eea), s + "sourceEA function doesn't fit to set of edge attribute edges");
        Category.ensure(sourceEA.trg().equals(Eg), s + "sourceEA function doesn't fit to set of graph edges");
        Category.ensure(targetEA.src().equals(Eea), s + "targetEA function doesn't fit to set of edge attribute edges");
        Category.ensure(targetEA.trg().equals(Vd), s + "targetEA function doesn't fit to set of data nodes");

        // Check for duplicate data nodes
        Category.ensure(new HashSet<>(Vd.elts()).size() == Vd.elts().size(), s + "duplicate data node");
    }
}
