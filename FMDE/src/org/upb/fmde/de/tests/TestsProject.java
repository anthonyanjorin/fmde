package org.upb.fmde.de.tests;

import org.junit.Test;
import org.upb.fmde.de.categories.concrete.egraphs.EGraph;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphMorphism;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.upb.fmde.de.categories.concrete.egraphs.EGraphs.eGraphs;

public class TestsProject {

    @Test
    public void testEGraphComposition() {

        EGraph hostGraph = createHostGraph();
        EGraph typeGraph = createTypeGraph();

        TotalFunction tVg = new TotalFunction(hostGraph.Vg, "tVg", typeGraph.Vg);
        TotalFunction tVd = new TotalFunction(hostGraph.Vd, "tVd", typeGraph.Vd);
        TotalFunction tEg = new TotalFunction(hostGraph.Eg, "tEg", typeGraph.Eg);
        TotalFunction tEna = new TotalFunction(hostGraph.Ena, "tEna", typeGraph.Ena);
        TotalFunction tEea = new TotalFunction(hostGraph.Eea, "tEea", typeGraph.Eea);

        tVg.addMapping("node1", "Player");
        tVg.addMapping("node2", "Player");
        tVd.addMapping("yellow", "Color");
        tVd.addMapping("blue", "Color");
        tEg.addMapping("edge1to2", "PlayerEdge");
        tEna.addMapping("node1.color", "Player.color");
        tEna.addMapping("node2.color", "Player.color");
        tEea.addMapping("edge1to2.color", "PlayerEdge.color");

        EGraphMorphism type = new EGraphMorphism("type", hostGraph, typeGraph, tVg, tVd, tEg, tEna, tEea);

        assertTrue(eGraphs.compose(eGraphs.id(hostGraph), type).isTheSameAs(type));
        assertTrue(eGraphs.compose(type, eGraphs.id(typeGraph)).isTheSameAs(type));
    }

    @Test
    public void testEGraphPatternMatcher() {
        EGraph hostGraph = createHostGraph();
        EGraph patternGraph = createPatternGraph();

        EGraphPatternMatcher matcher = new EGraphPatternMatcher(patternGraph, hostGraph);
        List<EGraphMorphism> matches = matcher.determineMatches(false);
        int i = 5;
    }

    private EGraph createPatternGraph() {
        FinSet vg = new FinSet("graph nodes", "node");
        FinSet vd = new FinSet("data nodes", "yellow");

        FinSet eg = new FinSet("graph edges");
        FinSet ena = new FinSet("node attribute edges", "node.color");
        FinSet eea = new FinSet("edge attribute edges");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("node.color", "node");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("node.color", "yellow");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);

        return new EGraph("PatternGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }

    private EGraph createHostGraph() {
        FinSet vg = new FinSet("graph nodes", "node1", "node2");
        FinSet vd = new FinSet("data nodes", "yellow", "blue");

        FinSet eg = new FinSet("graph edges", "edge1to2");
        FinSet ena = new FinSet("node attribute edges", "node1.color", "node2.color");
        FinSet eea = new FinSet("edge attribute edges", "edge1to2.color");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        sourceG.addMapping("edge1to2", "node1");

        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);
        targetG.addMapping("edge1to2", "node2");

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("node1.color", "node1");
        sourceNA.addMapping("node2.color", "node2");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("node1.color", "yellow");
        targetNA.addMapping("node2.color", "blue");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        sourceEA.addMapping("edge1to2.color", "edge1to2");

        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);
        targetEA.addMapping("edge1to2.color", "yellow");

        return new EGraph("HostGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }

    private EGraph createTypeGraph() {
        FinSet vg = new FinSet("graph nodes", "Player");
        FinSet vd = new FinSet("data nodes", "Color");

        FinSet eg = new FinSet("graph edges", "PlayerEdge");
        FinSet ena = new FinSet("node attribute edges", "Player.color");
        FinSet eea = new FinSet("edge attribute edges", "PlayerEdge.color");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        sourceG.addMapping("PlayerEdge", "Player");

        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);
        targetG.addMapping("PlayerEdge", "Player");

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("Player.color", "Player");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("Player.color", "Color");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        sourceEA.addMapping("PlayerEdge.color", "PlayerEdge");

        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);
        targetEA.addMapping("PlayerEdge.color", "Color");

        return new EGraph("TypeGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }
}
